package ru.yandex.practicum.dto.commerce.payment;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class PaymentDto {

    UUID paymentId;

    BigDecimal totalPayment;

    BigDecimal productTotal;

    BigDecimal deliveryTotal;

    BigDecimal feeTotal;
}
