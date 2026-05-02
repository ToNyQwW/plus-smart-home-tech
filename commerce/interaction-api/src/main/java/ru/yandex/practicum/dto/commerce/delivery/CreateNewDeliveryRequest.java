package ru.yandex.practicum.dto.commerce.delivery;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.dto.commerce.AddressRequest;
import ru.yandex.practicum.model.DeliveryState;

import java.util.UUID;

@Value
@Builder
public class CreateNewDeliveryRequest {

    @Valid
    @NotNull
    AddressRequest fromAddress;

    @Valid
    @NotNull
    AddressRequest toAddress;

    @NotNull
    UUID orderId;

    @NotNull
    DeliveryState deliveryState;
}
