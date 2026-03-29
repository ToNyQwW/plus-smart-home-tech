package ru.yandex.practicum.service.handler.hubEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.service.SensorService;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.service.handler.HubEventHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler<DeviceAddedEventAvro> {

    private final SensorService sensorService;

    @Override
    public Class<DeviceAddedEventAvro> getPayloadType() {
        return DeviceAddedEventAvro.class;
    }

    @Override
    public void handle(String hubId, HubEventAvro event) {
        DeviceAddedEventAvro deviceAddedEventAvro = (DeviceAddedEventAvro) event.getPayload();
        sensorService.save(deviceAddedEventAvro, hubId);
        log.info("В хабе [{}] зарегистрирован новый датчик: [{}]", hubId, deviceAddedEventAvro.getId());
    }
}