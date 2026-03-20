package ru.yandex.practicum.service.handler.sensorEvent;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.model.sensorEvent.MotionSensorEvent;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;
import ru.yandex.practicum.model.sensorEvent.SensorEventType;
import ru.yandex.practicum.service.KafkaEventProducer;

@Service
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {

    public MotionSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventType getSensorEventType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEvent sensorEvent) {
        MotionSensorEvent event = (MotionSensorEvent) sensorEvent;

        return MotionSensorAvro.newBuilder()
                .setMotion(event.isMotion())
                .setLinkQuality(event.getLinkQuality())
                .setVoltage(event.getVoltage())
                .build();
    }
}