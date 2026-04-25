package ru.yandex.practicum.dto.commerce;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.model.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class OrderRequest {

    @NotNull
    UUID orderId;

    @NotNull
    UUID shoppingCartId;

    @NotEmpty
    Map<@NotNull UUID, @Positive Long> products;

    @NotNull
    UUID paymentId;

    @NotNull
    UUID deliveryId;

    @NotNull
    OrderState state;

    @Positive
    double deliveryWeight;

    @Positive
    double deliveryVolume;

    boolean fragile;

    @Positive
    BigDecimal totalPrice;

    @Positive
    BigDecimal deliveryPrice;

    @Positive
    BigDecimal productPrice;
}
