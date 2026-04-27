package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.commerce.payment.CreatePaymentRequest;
import ru.yandex.practicum.dto.commerce.payment.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }
}
