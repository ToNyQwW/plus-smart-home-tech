package ru.yandex.practicum.dto.commerce.payment;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CalculateTotalCostRequest {

    @Positive
    BigDecimal productPrice;

    @Positive
    BigDecimal deliveryPrice;
}
