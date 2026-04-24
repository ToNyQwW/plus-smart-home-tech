package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class ShoppingCartRequest {

    @NotNull
    UUID shoppingCartId;

    @NotNull
    Map<@NotNull UUID, @Positive Long> products;
}
