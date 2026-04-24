package ru.yandex.practicum.dto.commerce.store;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.model.QuantityState;

import java.util.UUID;

@Value
@Builder
public class SetProductQuantityStateRequest {

    @NotNull
    UUID productId;

    @NotNull
    QuantityState quantityState;
}