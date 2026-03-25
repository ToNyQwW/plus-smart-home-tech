package ru.yandex.practicum.service.handler;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventHandler {

    HubEventProto.PayloadCase getHubEventType();

    void handle(HubEventProto event);
}