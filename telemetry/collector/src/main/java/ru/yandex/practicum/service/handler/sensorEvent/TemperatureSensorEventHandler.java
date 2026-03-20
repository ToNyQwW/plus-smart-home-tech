package ru.yandex.practicum.service.handler.sensorEvent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;
import ru.yandex.practicum.model.sensorEvent.TemperatureSensorEvent;

@Component("TEMPERATURE_SENSOR_EVENT")
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {

    public TemperatureSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent sensorEvent) {
        TemperatureSensorEvent event = (TemperatureSensorEvent) sensorEvent;
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
    }
}