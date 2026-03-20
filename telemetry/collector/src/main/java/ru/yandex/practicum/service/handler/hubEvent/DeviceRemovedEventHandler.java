package ru.yandex.practicum.service.handler.hubEvent;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.service.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.model.hubEvent.DeviceRemovedEvent;
import ru.yandex.practicum.model.hubEvent.HubEvent;

@Component("DEVICE_REMOVED")
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {

    public DeviceRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEvent hubEvent) {
        DeviceRemovedEvent event = (DeviceRemovedEvent) hubEvent;

        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }
}