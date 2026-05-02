package ru.yandex.practicum.dto.commerce.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class NewProductInWarehouseRequest {

    @NotNull
    UUID productId;

    boolean fragile;

    @Valid
    @NotNull
    DimensionDto dimension;

    @Min(1)
    double weight;
}