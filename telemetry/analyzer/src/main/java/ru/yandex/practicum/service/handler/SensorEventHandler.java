package ru.yandex.practicum.service.handler;

import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

public interface SensorEventHandler<T> {

    Class<T> getPayloadType();

    Integer getValue(ConditionTypeAvro conditionType, SensorStateAvro sensorState);
}