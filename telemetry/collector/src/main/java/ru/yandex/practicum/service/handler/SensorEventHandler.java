package ru.yandex.practicum.service.handler;

import ru.yandex.practicum.model.sensorEvent.SensorEvent;
import ru.yandex.practicum.model.sensorEvent.SensorEventType;

public interface SensorEventHandler {

    SensorEventType getSensorEventType();

    void handle(SensorEvent event);
}