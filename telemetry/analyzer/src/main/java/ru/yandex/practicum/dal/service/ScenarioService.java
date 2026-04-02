package ru.yandex.practicum.dal.service;

import ru.yandex.practicum.dal.entity.Scenario;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

public interface ScenarioService {

    Scenario save(ScenarioAddedEventAvro event, String hubId);

    void delete(String name, String hubId);
}