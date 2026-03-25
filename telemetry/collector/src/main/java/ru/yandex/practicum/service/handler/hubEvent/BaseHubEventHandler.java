package ru.yandex.practicum.service.handler.hubEvent;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.KafkaTopics;
import ru.yandex.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.service.handler.HubEventHandler;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    protected final KafkaEventProducer producer;

    protected abstract T mapToAvro(HubEventProto hubEvent);

    @Override
    public void handle(HubEventProto event) {

        T payload = mapToAvro(event);

        Instant timestamp = Instant.ofEpochSecond(
                event.getTimestamp().getSeconds(),
                event.getTimestamp().getNanos()
        );

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();

        producer.send(eventAvro, event.getHubId(), timestamp, KafkaTopics.HUB);
    }
}