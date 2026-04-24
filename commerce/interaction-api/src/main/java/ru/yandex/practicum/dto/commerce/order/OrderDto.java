package ru.yandex.practicum.dto.commerce.order;

import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.model.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class OrderDto {

    UUID orderId;

    UUID shoppingCartId;

    Map<UUID, Long> products;

    UUID paymentId;

    UUID deliveryId;

    OrderState state;

    double deliveryWeight;

    double deliveryVolume;

    boolean fragile;

    BigDecimal totalPrice;

    BigDecimal deliveryPrice;

    BigDecimal productPrice;
}
