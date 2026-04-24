package ru.yandex.practicum.dto.commerce.warehouse;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookedProductsDto {

    double deliveryWeight;

    double deliveryVolume;

    boolean fragile;
}