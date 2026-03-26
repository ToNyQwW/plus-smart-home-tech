package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SnapshotServiceImpl implements SnapshotService {

    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> update(SensorEventAvro event) {

        Instant timestamp = event.getTimestamp();
        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(
                event.getHubId(),
                hubId ->
                        SensorsSnapshotAvro.newBuilder()
                                .setHubId(hubId)
                                .setTimestamp(timestamp)
                                .setSensorsState(new HashMap<>())
                                .build()
        );

        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();
        String id = event.getId();

        SensorStateAvro oldState = sensorsState.get(id);
        if (oldState != null) {
            if (oldState.getTimestamp().isAfter(timestamp) ||
                    oldState.getData().equals(event.getPayload())) {
                return Optional.empty();
            }
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(timestamp)
                .setData(event.getPayload())
                .build();

        sensorsState.put(id, newState);
        snapshot.setTimestamp(timestamp);

        return Optional.of(snapshot);
    }
}