package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.payment.CalculateTotalCostRequest;
import ru.yandex.practicum.dto.commerce.payment.CreatePaymentRequest;
import ru.yandex.practicum.dto.commerce.payment.PaymentDto;

import java.math.BigDecimal;

public interface PaymentService {

    PaymentDto createPayment(CreatePaymentRequest request);

    BigDecimal calculateTotalCost(CalculateTotalCostRequest request);
}
