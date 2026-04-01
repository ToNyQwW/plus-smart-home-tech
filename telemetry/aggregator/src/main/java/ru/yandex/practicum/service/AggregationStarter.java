package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.configuration.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class AggregationStarter {

    private final SnapshotService snapshotService;
    private final KafkaConfig.ProducerConfig producerConfig;
    private final KafkaConfig.ConsumerConfig consumerConfig;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();


    public AggregationStarter(KafkaConfig kafkaConfig, SnapshotService snapshotService) {
        this.snapshotService = snapshotService;
        this.producerConfig = kafkaConfig.getProducer();
        this.consumerConfig = kafkaConfig.getConsumer();
        this.producer = new KafkaProducer<>(producerConfig.getProperties());
        this.consumer = new KafkaConsumer<>(consumerConfig.getProperties());

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
    }

    public void start() {
        try {
            consumer.subscribe(List.of(consumerConfig.getTopic()));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(consumerConfig.getPollTimeout());

                if (records.isEmpty()) {
                    continue;
                }

                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    log.trace("Обработка показания датчика от хаба {} из партиции {} со смещением: {}",
                            record.key(), record.partition(), record.offset());
                    handleEvent(record.value());
                    manageOffsets(record, count++);
                }
                producer.flush();
                consumer.commitAsync();
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
            } finally {
                consumer.close();
                producer.close();
            }
        }
    }

    private void handleEvent(SensorEventAvro event) {
        Optional<SensorsSnapshotAvro> updatedState = snapshotService.update(event);

        if (updatedState.isEmpty()) {
            log.info("Событие датчика {} не обновило состояние снапшота.", event.getId());
            return;
        }

        SensorsSnapshotAvro sensorsSnapshot = updatedState.get();

        String hubId = sensorsSnapshot.getHubId();
        Instant timestamp = sensorsSnapshot.getTimestamp();
        String topic = producerConfig.getTopic();

        ProducerRecord<String, SensorsSnapshotAvro> record = new ProducerRecord<>(
                topic,
                null,
                timestamp.toEpochMilli(),
                hubId,
                sensorsSnapshot
        );

        log.info("Событие датчика {} обновило состояние снапшота. " +
                        "Сохранение снапшота состояния датчиков хаба {} от {} в топик {}",
                event.getId(), hubId, timestamp, topic);

        producer.send(record);
    }

    private void manageOffsets(ConsumerRecord<String, SensorEventAvro> record, int count) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % consumerConfig.getCommitBatchSize() == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }
}