package ru.yandex.practicum.service.handler.hubEvent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.hubEvent.DeviceAction;
import ru.yandex.practicum.model.hubEvent.HubEvent;
import ru.yandex.practicum.model.hubEvent.ScenarioAddedEvent;
import ru.yandex.practicum.model.hubEvent.ScenarioCondition;

import java.util.List;

@Component("SCENARIO_ADDED")
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent hubEvent) {
        ScenarioAddedEvent event = (ScenarioAddedEvent) hubEvent;

        List<DeviceActionAvro> deviceActionsAvro = event.getActions().stream()
                .map(this::mapToAvro)
                .toList();

        List<ScenarioConditionAvro> scenarioConditionsAvro = event.getConditions().stream()
                .map(this::mapToAvro)
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setActions(deviceActionsAvro)
                .setConditions(scenarioConditionsAvro)
                .build();
    }

    private DeviceActionAvro mapToAvro(DeviceAction deviceAction) {
        ActionTypeAvro actionTypeAvro = ActionTypeAvro.valueOf(deviceAction.getType().name());

        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setValue(deviceAction.getValue())
                .setType(actionTypeAvro)
                .build();
    }

    private ScenarioConditionAvro mapToAvro(ScenarioCondition scenarioCondition) {

        ConditionTypeAvro conditionTypeAvro = ConditionTypeAvro.valueOf(scenarioCondition.getType().name());
        ConditionOperationAvro conditionOperationAvro = ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name());

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setValue(scenarioCondition.getValue())
                .setType(conditionTypeAvro)
                .setOperation(conditionOperationAvro)
                .build();
    }
}