package ru.yandex.practicum.service.handler.sensorEvent;

import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.service.handler.SensorEventHandler;

public class TemperatureSensorEventHandler implements SensorEventHandler<TemperatureSensorAvro> {

    @Override
    public Class<TemperatureSensorAvro> getPayloadType() {
        return TemperatureSensorAvro.class;
    }

    @Override
    public Integer getValue(ConditionTypeAvro conditionType, SensorStateAvro sensorState) {
        TemperatureSensorAvro temperatureSensorAvro = (TemperatureSensorAvro) sensorState.getData();

        return switch (conditionType) {
            case TEMPERATURE -> temperatureSensorAvro.getTemperatureC();
            default -> null;
        };
    }
}