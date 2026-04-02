package ru.yandex.practicum.service.handler.sensorEvent;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.KafkaTopics;
import ru.yandex.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.service.handler.SensorEventHandler;

import java.time.Instant;

@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {

    protected final KafkaEventProducer producer;

    protected abstract T mapToAvro(SensorEventProto sensorEvent);

    @Override
    public void handle(SensorEventProto event) {

        T payload = mapToAvro(event);

        Instant timestamp = Instant.ofEpochSecond(
                event.getTimestamp().getSeconds(),
                event.getTimestamp().getNanos()
        );

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();

        producer.send(eventAvro, event.getHubId(), timestamp, KafkaTopics.SENSOR);
    }
}