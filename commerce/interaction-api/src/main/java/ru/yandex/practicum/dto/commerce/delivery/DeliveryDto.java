package ru.yandex.practicum.dto.commerce.delivery;

import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.model.DeliveryState;

import java.util.UUID;

@Value
@Builder
public class DeliveryDto {

    UUID deliveryId;

    AddressDto fromAddress;

    AddressDto toAddress;

    UUID orderId;

    DeliveryState deliveryState;
}
