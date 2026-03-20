package ru.yandex.practicum.service.handler;

import ru.yandex.practicum.model.hubEvent.HubEvent;

public interface HubEventHandler {

    void handle(HubEvent event);
}