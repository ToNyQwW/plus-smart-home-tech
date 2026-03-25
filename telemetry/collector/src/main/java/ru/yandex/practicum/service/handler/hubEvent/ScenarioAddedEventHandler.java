package ru.yandex.practicum.service.handler.hubEvent;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.service.KafkaEventProducer;

import java.util.List;

import static ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto.ValueCase.BOOL_VALUE;

@Service
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventProto.PayloadCase getHubEventType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto hubEvent) {
        ScenarioAddedEventProto scenarioAddedEvent = hubEvent.getScenarioAdded();

        List<DeviceActionAvro> deviceActionsAvro = scenarioAddedEvent.getActionList().stream()
                .map(this::mapToAvro)
                .toList();

        List<ScenarioConditionAvro> scenarioConditionsAvro = scenarioAddedEvent.getConditionList().stream()
                .map(this::mapToAvro)
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setActions(deviceActionsAvro)
                .setConditions(scenarioConditionsAvro)
                .build();
    }

    private DeviceActionAvro mapToAvro(DeviceActionProto deviceAction) {
        ActionTypeAvro actionTypeAvro = ActionTypeAvro.valueOf(deviceAction.getType().name());

        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setValue(deviceAction.getValue())
                .setType(actionTypeAvro)
                .build();
    }

    private ScenarioConditionAvro mapToAvro(ScenarioConditionProto scenarioCondition) {

        ConditionTypeAvro conditionTypeAvro = ConditionTypeAvro.valueOf(scenarioCondition.getType().name());
        ConditionOperationAvro conditionOperationAvro = ConditionOperationAvro.valueOf(scenarioCondition.getOperation().name());

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setValue(scenarioCondition.getValueCase().equals(BOOL_VALUE) ?
                        scenarioCondition.getBoolValue() : scenarioCondition.getIntValue())
                .setType(conditionTypeAvro)
                .setOperation(conditionOperationAvro)
                .build();
    }
}