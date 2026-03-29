package ru.yandex.practicum.service.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler<T> {

    Class<T> getPayloadType();

    void handle(String hubId, HubEventAvro event);
}