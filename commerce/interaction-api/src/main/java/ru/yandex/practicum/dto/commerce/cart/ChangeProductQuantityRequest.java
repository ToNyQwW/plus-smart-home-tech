package ru.yandex.practicum.dto.commerce.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ChangeProductQuantityRequest {

    @NotNull
    UUID productId;

    @Min(1)
    long newQuantity;
}