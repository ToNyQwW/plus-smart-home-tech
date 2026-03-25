package ru.yandex.practicum.service.handler;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventHandler {

    SensorEventProto.PayloadCase getSensorEventType();

    void handle(SensorEventProto event);
}