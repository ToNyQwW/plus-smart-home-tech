package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.hubEvent.HubEvent;
import ru.yandex.practicum.model.hubEvent.HubEventType;
import ru.yandex.practicum.service.handler.HubEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(path = "/events/hubs")
public class HubEventController {

    private final Map<HubEventType, HubEventHandler> hubEventHandlers;

    public HubEventController(Set<HubEventHandler> hubEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getHubEventType, Function.identity()));
    }

    @PostMapping
    public void sendHubEvent(@Valid @RequestBody HubEvent request) {
        HubEventHandler hubEventHandler = hubEventHandlers.get(request.getHubEventType());
        hubEventHandler.handle(request);
    }
}