package ru.yandex.practicum.dal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dal.entity.Sensor;
import ru.yandex.practicum.dal.repository.SensorRepository;
import ru.yandex.practicum.exception.DeviceAlreadyExistsException;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;

    @Override
    public Sensor save(DeviceAddedEventAvro event, String hubId) {
        //по скрипту из задания на создание таблиц не хранится DeviceTypeAvro, который приходит в сообщении
        String id = event.getId();
        Optional<Sensor> sensorOpt = sensorRepository.findByIdAndHubId(id, hubId);

        if (sensorOpt.isPresent()) {
            throw new DeviceAlreadyExistsException("Устройство с id " + id + " уже присутствует в хабе " + hubId);
        }

        Sensor sensor = new Sensor();
        sensor.setId(id);
        sensor.setHubId(hubId);

        return sensorRepository.save(sensor);
    }

    @Override
    public void delete(String id, String hubId) {
        sensorRepository.findByIdAndHubId(id, hubId).ifPresent(sensorRepository::delete);
    }
}