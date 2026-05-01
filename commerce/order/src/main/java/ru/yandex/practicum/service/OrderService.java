package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.commerce.order.OrderDto;
import ru.yandex.practicum.dto.commerce.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto createOrder(String username, CreateNewOrderRequest request);

    OrderDto calculateDeliveryPrice(UUID orderId);

    OrderDto calculateTotalPrice(UUID orderId);

    OrderDto executePayment(UUID orderId);

    OrderDto paymentSuccess(UUID orderId);

    OrderDto paymentFailed(UUID orderId);

    OrderDto assemblyOrder(UUID orderId);

    OrderDto assemblyOrderFailed(UUID orderId);

    OrderDto deliverySuccessful(UUID orderId);

    OrderDto deliveryOrderFailed(UUID orderId);

    OrderDto completedOrder(UUID orderId);

    OrderDto returnOrder(ProductReturnRequest request);

    List<OrderDto> getUserOrders(String username);
}
