package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.entity.Condition;
import ru.yandex.practicum.dal.entity.Scenario;
import ru.yandex.practicum.dal.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.handler.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SnapshotAnalyzer {

    private final ScenarioRepository scenarioRepository;
    private final Map<Class<?>, SensorEventHandler<?>> sensorEventHandlers;

    public SnapshotAnalyzer(ScenarioRepository scenarioRepository, List<SensorEventHandler<?>> handlers) {
        this.scenarioRepository = scenarioRepository;
        this.sensorEventHandlers = handlers.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getPayloadType,
                        Function.identity()
                ));
    }

    public List<Scenario> process(SensorsSnapshotAvro snapshot) {
        return scenarioRepository
                .findByHubId(snapshot.getHubId())
                .stream()
                .filter(scenario -> isConditionsMatchSnapshot(snapshot, scenario.getConditions()))
                .toList();
    }

    private boolean isConditionsMatchSnapshot(SensorsSnapshotAvro snapshot, Map<String, Condition> conditions) {
        return conditions
                .entrySet()
                .stream()
                .allMatch(conditionEntry ->
                        checkCondition(conditionEntry.getKey(), conditionEntry.getValue(), snapshot)
                );
    }

    private boolean checkCondition(String sensorId, Condition condition, SensorsSnapshotAvro snapshot) {
        if (!snapshot.getSensorsState().containsKey(sensorId)) {
            return false;
        }

        SensorStateAvro sensorStateAvro = snapshot.getSensorsState().get(sensorId);
        SensorEventHandler<?> sensorEventHandler = sensorEventHandlers.get(sensorStateAvro.getData().getClass());

        if (sensorEventHandler == null) {
            log.warn("Нет обработчика для {}", sensorStateAvro.getData().getClass());
            return false;
        }

        Integer value = sensorEventHandler.getValue(condition.getType(), sensorStateAvro);

        if (value == null) {
            return false;
        }

        return switch (condition.getOperation()) {
            case EQUALS -> value.equals(condition.getValue());
            case LOWER_THAN -> value < condition.getValue();
            case GREATER_THAN -> value > condition.getValue();
        };
    }
}