package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;
import ru.yandex.practicum.model.sensorEvent.SensorEventType;
import ru.yandex.practicum.service.handler.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(path = "/events/sensorc")
public class SensorEventController {


    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;

    public SensorEventController(List<SensorEventHandler> sensorEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getSensorEventType, Function.identity()));
    }

    @PostMapping
    public void sendSensorEvent(@Valid @RequestBody SensorEvent request) {
        SensorEventHandler sensorEventHandler = sensorEventHandlers.get(request.getSensorEventType());
        sensorEventHandler.handle(request);
    }
}