package ru.yandex.practicum.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ChangeProductQuantityRequest {

    @NotNull
    UUID productId;

    @NotNull
    @PositiveOrZero
    Long newQuantity;
}