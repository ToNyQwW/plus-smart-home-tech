package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.OrderRequest;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;

import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createNewDelivery(CreateNewDeliveryRequest request);

    DeliveryDto completeDelivery(UUID orderId);

    DeliveryDto startDelivery(UUID orderId);

    DeliveryDto failDelivery(UUID orderId);

    double calculateDeliveryCost(OrderRequest request);
}
