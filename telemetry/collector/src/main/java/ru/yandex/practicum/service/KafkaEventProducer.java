package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.configuration.KafkaConfig;
import ru.yandex.practicum.model.KafkaTopics;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
public class KafkaEventProducer implements AutoCloseable {

    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final Map<String, String> topics;

    public KafkaEventProducer(KafkaConfig kafkaConfig) {
        this.topics = kafkaConfig.getProducer().getTopics();
        this.producer = new KafkaProducer<>(kafkaConfig.getProducer().getProperties());
    }

    public void send(SpecificRecordBase event, String hubId, Instant timestamp, KafkaTopics kafkaTopic) {
        String topic = topics.get(kafkaTopic.name().toLowerCase());

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                timestamp.toEpochMilli(),
                hubId,
                event
        );

        log.trace("Сохранение события {} связанное с хабом {} в топик {}",
                event.getClass().getSimpleName(), hubId, topic);

        producer.send(record);
    }

    @Override
    public void close() {
        producer.flush();
        producer.close(Duration.ofSeconds(10));
    }
}