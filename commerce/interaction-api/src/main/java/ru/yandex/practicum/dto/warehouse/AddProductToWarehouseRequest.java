package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class AddProductToWarehouseRequest {

    @NotNull
    UUID productId;


    long quantity;
}