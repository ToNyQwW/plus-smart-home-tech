package ru.yandex.practicum.dto.commerce.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class CreatePaymentRequest {

    @NotNull
    UUID orderId;

    @NotNull
    UUID paymentId;

    @Positive
    BigDecimal totalPrice;

    @Positive
    BigDecimal deliveryPrice;

    @Positive
    BigDecimal productPrice;
}
