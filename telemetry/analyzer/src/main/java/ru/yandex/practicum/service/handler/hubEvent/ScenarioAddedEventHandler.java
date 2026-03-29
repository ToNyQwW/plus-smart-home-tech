package ru.yandex.practicum.service.handler.hubEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.service.ScenarioService;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.service.handler.HubEventHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {

    private final ScenarioService scenarioService;

    @Override
    public String getType() {
        return ScenarioAddedEventAvro.class.getName();
    }

    @Override
    public void handle(String hubId, HubEventAvro event) {
        ScenarioAddedEventAvro scenarioAddedEventAvro = (ScenarioAddedEventAvro) event.getPayload();

        log.info("запрос на добавление нового сценария {} для хаба {}", scenarioAddedEventAvro.getName(), hubId);
        scenarioService.save(scenarioAddedEventAvro, hubId);
    }
}