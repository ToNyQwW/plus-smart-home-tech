package ru.yandex.practicum.service.handler.sensorEvent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;
import ru.yandex.practicum.model.sensorEvent.SwitchSensorEvent;

@Component("SWITCH_SENSOR_EVENT")
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent sensorEvent) {
        SwitchSensorEvent event = (SwitchSensorEvent) sensorEvent;

        return SwitchSensorAvro.newBuilder()
                .setState(event.isState())
                .build();
    }
}