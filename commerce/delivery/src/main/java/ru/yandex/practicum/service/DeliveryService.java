package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;

public interface DeliveryService {

    DeliveryDto createNewDelivery(CreateNewDeliveryRequest request);
}
