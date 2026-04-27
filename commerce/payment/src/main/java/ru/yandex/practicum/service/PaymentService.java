package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.payment.CreatePaymentRequest;
import ru.yandex.practicum.dto.commerce.payment.PaymentDto;

public interface PaymentService {

    PaymentDto createPayment(CreatePaymentRequest request);
}
