package ru.yandex.practicum.service.handler.sensorEvent;

import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.service.handler.SensorEventHandler;

public class LightSensorEventHandler implements SensorEventHandler {

    @Override
    public String getType() {
        return LightSensorAvro.class.getName();
    }

    @Override
    public Integer getValue(ConditionTypeAvro conditionType, SensorStateAvro sensorState) {
        LightSensorAvro lightSensorAvro = (LightSensorAvro) sensorState.getData();

        return switch (conditionType) {
            case LUMINOSITY -> lightSensorAvro.getLuminosity();
            default -> null;
        };
    }
}