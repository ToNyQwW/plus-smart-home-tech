package ru.yandex.practicum.dto.commerce.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class ProductReturnRequest {

    @NotNull
    UUID orderId;

    @NotEmpty
    Map<@NotNull UUID, @Positive Long> products;
}
