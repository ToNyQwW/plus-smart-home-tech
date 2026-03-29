package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.configuration.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.service.handler.HubEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final KafkaConfig.ConsumerConfig kafkaConfig;
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final Map<String, HubEventHandler> hubEventHandlers;

    public HubEventProcessor(KafkaConfig kafkaConfig, List<HubEventHandler> handlers) {
        this.kafkaConfig = kafkaConfig.getHubEventConsumer();
        this.consumer = new KafkaConsumer<>(kafkaConfig.getHubEventConsumer().getProperties());
        this.hubEventHandlers = handlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getType,
                        Function.identity()
                ));

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of(kafkaConfig.getTopic()));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(kafkaConfig.getPollTimeout());

                if (records.isEmpty()) {
                    continue;
                }

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    Object payload = record.value().getPayload();
                    HubEventHandler hubEventHandler = hubEventHandlers.get(payload.getClass());
                    hubEventHandler.handle(record.key(), record.value());
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
}