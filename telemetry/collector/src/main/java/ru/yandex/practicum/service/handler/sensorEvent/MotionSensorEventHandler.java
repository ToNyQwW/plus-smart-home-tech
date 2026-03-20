package ru.yandex.practicum.service.handler.sensorEvent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.model.sensorEvent.MotionSensorEvent;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;

@Component("MOTION_SENSOR_EVENT")
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {

    public MotionSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
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