package ru.yandex.practicum.service.handler.sensorEvent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.service.handler.SensorEventHandler;

@Component
public class MotionSensorEventHandler implements SensorEventHandler {

    @Override
    public String getPayloadType() {
        return MotionSensorAvro.class.getName();
    }

    @Override
    public Integer getValue(ConditionTypeAvro conditionType, SensorStateAvro sensorState) {
        MotionSensorAvro motionSensorAvro = (MotionSensorAvro) sensorState.getData();

        return switch (conditionType) {
            case MOTION -> motionSensorAvro.getMotion() ? 1 : 0;
            default -> null;
        };
    }
}