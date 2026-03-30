package ru.yandex.practicum.dal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dal.entity.Action;
import ru.yandex.practicum.dal.entity.Condition;
import ru.yandex.practicum.dal.entity.Scenario;
import ru.yandex.practicum.dal.repository.ActionRepository;
import ru.yandex.practicum.dal.repository.ConditionRepository;
import ru.yandex.practicum.dal.repository.ScenarioRepository;
import ru.yandex.practicum.dal.repository.SensorRepository;
import ru.yandex.practicum.exception.SensorNotFoundException;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScenarioServiceImpl implements ScenarioService {

    private final ActionRepository actionRepository;
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;

    @Override
    public Scenario save(ScenarioAddedEventAvro event, String hubId) {
        List<DeviceActionAvro> actions = event.getActions();
        List<ScenarioConditionAvro> conditions = event.getConditions();

        Set<String> sensorIds = actions.stream()
                .map(DeviceActionAvro::getSensorId)
                .collect(Collectors.toSet());

        conditions.stream()
                .map(ScenarioConditionAvro::getSensorId)
                .forEach(sensorIds::add);

        throwIfSensorsNotFound(sensorIds, hubId);

        String name = event.getName();
        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(hubId, name);

        Scenario scenario;
        if (scenarioOpt.isPresent()) {
            scenario = scenarioOpt.get();
            clearActions(scenario);
            clearConditions(scenario);
        } else {
            scenario = new Scenario();
            scenario.setHubId(hubId);
            scenario.setName(name);
        }

        for (DeviceActionAvro deviceAction : actions) {
            Action action = mapToAction(deviceAction);
            scenario.addAction(deviceAction.getSensorId(), action);
        }
        actionRepository.saveAll(scenario.getActions().values());

        for (ScenarioConditionAvro scenarioCondition : conditions) {
            Condition condition = mapToCondition(scenarioCondition);
            scenario.addCondition(scenarioCondition.getSensorId(), condition);
        }
        conditionRepository.saveAll(scenario.getConditions().values());

        return scenarioRepository.save(scenario);
    }

    @Override
    public void delete(String name, String hubId) {
        Optional<Scenario> scenarioOpt = scenarioRepository.findByHubIdAndName(hubId, name);
        if (scenarioOpt.isPresent()) {
            Scenario scenario = scenarioOpt.get();
            actionRepository.deleteAll(scenario.getActions().values());
            conditionRepository.deleteAll(scenario.getConditions().values());
            scenarioRepository.delete(scenario);
        }
    }

    private void throwIfSensorsNotFound(Set<String> sensorIds, String hubId) {
        if (!sensorRepository.existsByIdInAndHubId(sensorIds, hubId)) {
            throw new SensorNotFoundException("Не найден id сенсора при попытке добавить новый сценарий");
        }
    }

    private void clearActions(Scenario scenario) {
        Map<String, Action> actions = scenario.getActions();
        actionRepository.deleteAll(actions.values());
        scenario.getActions().clear();
    }

    private void clearConditions(Scenario scenario) {
        Map<String, Condition> conditions = scenario.getConditions();
        conditionRepository.deleteAll(conditions.values());
        scenario.getConditions().clear();
    }

    private Action mapToAction(DeviceActionAvro deviceAction) {
        Action action = new Action();
        action.setType(deviceAction.getType());
        action.setValue(deviceAction.getValue());
        return action;
    }

    private Integer mapValue(Object value) {
        if (value != null) {
            if (value instanceof Integer i) return i;
            if (value instanceof Boolean b) return b ? 1 : 0;
        }
        return null;
    }

    private Condition mapToCondition(ScenarioConditionAvro scenarioCondition) {
        Condition condition = new Condition();
        condition.setType(scenarioCondition.getType());
        condition.setOperation(scenarioCondition.getOperation());
        condition.setValue(mapValue(scenarioCondition.getValue()));
        return condition;
    }
}