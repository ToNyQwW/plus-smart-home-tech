package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.configuration.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.service.HubEventService;

import java.util.List;

@Slf4j
@Component
public class HubEventStarter implements Runnable {

    private final HubEventService hubEventService;
    private final KafkaConfig.ConsumerConfig kafkaConfig;
    private final KafkaConsumer<String, HubEventAvro> consumer;

    public HubEventStarter(KafkaConfig kafkaConfig, HubEventService hubEventService) {
        this.hubEventService = hubEventService;
        this.kafkaConfig = kafkaConfig.getHubEventConsumer();
        this.consumer = new KafkaConsumer<>(kafkaConfig.getHubEventConsumer().getProperties());
    }

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(kafkaConfig.getTopic()));
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(kafkaConfig.getPollTimeout());

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    handleRecord(record);
                }
                consumer.commitSync();
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки сообщений от хаба", e);
        } finally {

            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    private void handleRecord(ConsumerRecord<String, HubEventAvro> record) {
        hubEventService.handle(record.value());
    }
}