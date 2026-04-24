package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;

public interface OrderService {

    OrderDto createOrder(CreateNewOrderRequest request);
}
