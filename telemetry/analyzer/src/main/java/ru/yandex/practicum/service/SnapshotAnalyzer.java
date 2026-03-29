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

            boolean allConditionsMet = true;

            for (Map.Entry<String, Condition> entry : conditions.entrySet()) {
                String sensorId = entry.getKey();
                Condition condition = entry.getValue();

                SensorStateAvro sensorState = snapshot.getSensorsState().get(sensorId);

                if (sensorState == null) {
                    log.warn("Состояние сенсора {} отсутствует в снапшоте", sensorId);
                    allConditionsMet = false;
                    break;
                }

                SensorEventHandler<?> handler = sensorEventHandlers.get(sensorState.getData().getClass());
                if (handler == null) {
                    log.warn("Нет обработчика для типа сенсора {}", sensorState.getData().getClass());
                    allConditionsMet = false;
                    break;
                }

                Integer sensorValue = handler.getValue(condition.getType(), sensorState);

                ConditionOperationAvro operation = condition.getOperation();
                boolean conditionMet = switch (operation) {
                    case EQUALS -> sensorValue.equals(condition.getValue());
                    case LOWER_THAN -> sensorValue < condition.getValue();
                    case GREATER_THAN -> sensorValue > condition.getValue();
                };

                if (!conditionMet) {
                    allConditionsMet = false;
                    break;
                }
            }

            if (allConditionsMet) {
                result.add(scenario);
            }
        }

        return result;
    }
}