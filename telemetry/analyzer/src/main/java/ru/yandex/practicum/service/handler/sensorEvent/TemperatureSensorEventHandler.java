package ru.yandex.practicum.service.handler.sensorEvent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.service.handler.SensorEventHandler;

@Component
public class TemperatureSensorEventHandler implements SensorEventHandler {

    @Override
    public String getPayloadType() {
        return TemperatureSensorAvro.class.getName();
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