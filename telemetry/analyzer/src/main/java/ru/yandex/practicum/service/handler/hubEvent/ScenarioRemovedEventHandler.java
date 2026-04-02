package ru.yandex.practicum.service.handler.hubEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.service.ScenarioService;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.service.handler.HubEventHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScenarioRemovedEventHandler implements HubEventHandler {

    private final ScenarioService scenarioService;

    @Override
    public String getPayloadType() {
        return ScenarioRemovedEventAvro.class.getName();
    }

    @Override
    public void handle(String hubId, HubEventAvro event) {
        ScenarioRemovedEventAvro scenarioRemovedEventAvro = (ScenarioRemovedEventAvro) event.getPayload();

        String name = scenarioRemovedEventAvro.getName();
        log.info("Получен запрос на удаление сценария {} из хаба {}", name, hubId);
        scenarioService.delete(name, hubId);
    }
}