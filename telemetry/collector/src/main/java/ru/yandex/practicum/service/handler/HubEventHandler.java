package ru.yandex.practicum.service.handler;

import ru.yandex.practicum.model.hubEvent.HubEvent;
import ru.yandex.practicum.model.hubEvent.HubEventType;

public interface HubEventHandler {

    HubEventType getHubEventType();

    void handle(HubEvent event);
}