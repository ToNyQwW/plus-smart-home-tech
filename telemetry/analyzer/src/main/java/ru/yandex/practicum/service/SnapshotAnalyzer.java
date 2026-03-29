package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.entity.Condition;
import ru.yandex.practicum.dal.entity.Scenario;
import ru.yandex.practicum.dal.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.handler.SensorEventHandler;

import java.util.ArrayList;
import java.util.Collections;
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
        String hubId = snapshot.getHubId();
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);

        if (scenarios.isEmpty()) {
            log.info("Для хаба {} сценарии не найдены", hubId);
            return Collections.emptyList();
        }

        List<Scenario> result = new ArrayList<>();

        for (Scenario scenario : scenarios) {
            Map<String, Condition> conditions = scenario.getConditions();
            boolean flag = false;
            for (Map.Entry<String, Condition> values : conditions.entrySet()) {
                String sensorId = values.getKey();
                Condition condition = values.getValue();
                SensorStateAvro sensorStateAvro = snapshot.getSensorsState().get(sensorId);
                SensorEventHandler<?> sensorEventHandler = sensorEventHandlers.get(sensorStateAvro.getData().getClass());
                Integer value = sensorEventHandler.getValue(condition.getType(), sensorStateAvro);

                ConditionOperationAvro operation = condition.getOperation();
                flag = switch (operation) {
                    case EQUALS -> value.equals(condition.getValue());
                    case LOWER_THAN ->  value < condition.getValue();
                    case GREATER_THAN ->   value > condition.getValue();
                };
            }
            if (flag) {
                result.add(scenario);
            }
        }

        return result;
    }
}