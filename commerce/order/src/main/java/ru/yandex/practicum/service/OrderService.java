package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.commerce.order.OrderDto;

public interface OrderService {

    OrderDto createOrder(String username, CreateNewOrderRequest request);
}
