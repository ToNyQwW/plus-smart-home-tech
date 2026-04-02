package ru.yandex.practicum.service.handler.sensorEvent;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.service.KafkaEventProducer;

@Service
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {

    public TemperatureSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getSensorEventType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEventProto sensorEvent) {
        TemperatureSensorProto temperatureSensor = sensorEvent.getTemperatureSensor();

        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(temperatureSensor.getTemperatureC())
                .setTemperatureF(temperatureSensor.getTemperatureF())
                .build();
    }
}