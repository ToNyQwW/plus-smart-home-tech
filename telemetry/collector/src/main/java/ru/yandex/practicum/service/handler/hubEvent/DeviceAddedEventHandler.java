package ru.yandex.practicum.service.handler.hubEvent;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.model.hubEvent.DeviceAddedEvent;
import ru.yandex.practicum.model.hubEvent.HubEvent;
import ru.yandex.practicum.model.hubEvent.HubEventType;
import ru.yandex.practicum.service.KafkaEventProducer;

@Service
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventType getHubEventType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent hubEvent) {
        DeviceAddedEvent event = (DeviceAddedEvent) hubEvent;
        DeviceTypeAvro deviceTypeAvro = DeviceTypeAvro.valueOf(event.getType().name());

        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(deviceTypeAvro)
                .build();
    }
}