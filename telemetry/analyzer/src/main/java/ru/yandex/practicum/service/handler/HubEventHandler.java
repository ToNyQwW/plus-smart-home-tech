package ru.yandex.practicum.service.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {

    String getType();

    void handle(String hubId, HubEventAvro event);
}