package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.delivery.CalculateDeliveryCostRequest;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createNewDelivery(CreateNewDeliveryRequest request);

    DeliveryDto completeDelivery(UUID deliveryId);

    DeliveryDto startDelivery(UUID deliveryId);

    DeliveryDto failDelivery(UUID deliveryId);

    BigDecimal calculateDeliveryCost(CalculateDeliveryCostRequest request);
}
