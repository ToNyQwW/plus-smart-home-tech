package ru.yandex.practicum.service.handler.sensorEvent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.service.handler.SensorEventHandler;

@Component
public class ClimateSensorEventHandler implements SensorEventHandler<ClimateSensorAvro> {

    @Override
    public Class<ClimateSensorAvro> getPayloadType() {
        return ClimateSensorAvro.class;
    }

    @Override
    public Integer getValue(ConditionTypeAvro conditionType, SensorStateAvro sensorState) {
        ClimateSensorAvro climateSensorAvro = (ClimateSensorAvro) sensorState.getData();

        return switch (conditionType) {
            case TEMPERATURE -> climateSensorAvro.getTemperatureC();
            case HUMIDITY -> climateSensorAvro.getHumidity();
            case CO2LEVEL -> climateSensorAvro.getCo2Level();
            default -> null;
        };
    }
}