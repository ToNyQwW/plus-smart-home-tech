package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.OrderRequest;
import ru.yandex.practicum.dto.commerce.payment.PaymentDto;

public interface PaymentService {

    PaymentDto createPayment(OrderRequest request);
}
