package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.configuration.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.SnapshotService;

import java.util.List;

@Slf4j
@Component
public class SnapshotStarter {

    private final SnapshotService snapshotService;
    private final KafkaConfig.ConsumerConfig kafkaConfig;
    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;

    public SnapshotStarter(KafkaConfig kafkaConfig, SnapshotService snapshotService) {
        this.snapshotService = snapshotService;
        this.kafkaConfig = kafkaConfig.getSnapshotConsumer();
        this.consumer = new KafkaConsumer<>(this.kafkaConfig.getProperties());
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            consumer.subscribe(List.of(kafkaConfig.getTopic()));
            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(kafkaConfig.getPollTimeout());

                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    handleRecord(record);
                }
                consumer.commitSync();
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки снапшота датчиков", e);
        } finally {

            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorsSnapshotAvro> record) {
        snapshotService.handleSnapshot(record.value());
    }
}