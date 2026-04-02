package ru.yandex.practicum.dal.service;

import ru.yandex.practicum.dal.entity.Sensor;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;

public interface SensorService {

    Sensor save(DeviceAddedEventAvro event, String hubId);

    void delete(String id, String hubId);
}