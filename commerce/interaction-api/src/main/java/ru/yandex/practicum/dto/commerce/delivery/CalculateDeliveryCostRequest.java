package ru.yandex.practicum.dto.commerce.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CalculateDeliveryCostRequest {

    @NotNull
    UUID orderId;

    double deliveryWeight;

    double deliveryVolume;

    boolean fragile;
}
