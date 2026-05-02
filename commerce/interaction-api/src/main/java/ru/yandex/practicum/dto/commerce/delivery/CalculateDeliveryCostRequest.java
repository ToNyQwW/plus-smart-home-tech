package ru.yandex.practicum.dto.commerce.delivery;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CalculateDeliveryCostRequest {

    @NotNull
    UUID deliveryId;

    @Positive
    double deliveryWeight;

    @Positive
    double deliveryVolume;

    boolean fragile;
}
