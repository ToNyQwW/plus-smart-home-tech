package ru.yandex.practicum.service.handler.hubEvent;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.KafkaTopics;
import ru.yandex.practicum.model.hubEvent.HubEvent;
import ru.yandex.practicum.service.handler.HubEventHandler;

@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    protected final KafkaEventProducer producer;

    protected abstract T mapToAvro(HubEvent hubEvent);

    @Override
    public void handle(HubEvent event) {

        T payload = mapToAvro(event);

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

        producer.send(eventAvro, event.getHubId(), event.getTimestamp(), KafkaTopics.HUB);
    }
}