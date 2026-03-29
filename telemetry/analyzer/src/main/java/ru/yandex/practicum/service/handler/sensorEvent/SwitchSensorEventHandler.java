package ru.yandex.practicum.service.handler.sensorEvent;

import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.service.handler.SensorEventHandler;

public class SwitchSensorEventHandler implements SensorEventHandler<SwitchSensorAvro> {

    @Override
    public Class<SwitchSensorAvro> getPayloadType() {
        return SwitchSensorAvro.class;
    }

    @Override
    public Integer getValue(ConditionTypeAvro conditionType, SensorStateAvro sensorState) {
        SwitchSensorAvro switchSensorAvro = (SwitchSensorAvro) sensorState.getData();

        return switch (conditionType) {
            case SWITCH -> switchSensorAvro.getState() ? 1 : 0;
            default -> 0;
        };
    }
}