package ru.yandex.practicum.dto.commerce.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ShippedToDeliveryRequest {

    @NotNull
    UUID orderId;

    @NotNull
    UUID deliveryId;
}
