package ru.yandex.practicum.service.handler;

import ru.yandex.practicum.model.sensorEvent.SensorEvent;

public interface SensorEventHandler {

    void handle(SensorEvent event);
}