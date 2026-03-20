package ru.yandex.practicum.service.handler.hubEvent;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.model.hubEvent.HubEvent;
import ru.yandex.practicum.model.hubEvent.HubEventType;
import ru.yandex.practicum.model.hubEvent.ScenarioRemovedEvent;
import ru.yandex.practicum.service.KafkaEventProducer;

@Service
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    public ScenarioRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventType getHubEventType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent hubEvent) {
        ScenarioRemovedEvent event = (ScenarioRemovedEvent) hubEvent;


        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }
}