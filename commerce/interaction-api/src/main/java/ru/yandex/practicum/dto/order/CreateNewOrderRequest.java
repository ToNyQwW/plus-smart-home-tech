package ru.yandex.practicum.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateNewOrderRequest {

    @Valid
    @NotNull
    ShoppingCartRequest shoppingCart;

    @Valid
    @NotNull
    AddressRequest deliveryAddress;
}
