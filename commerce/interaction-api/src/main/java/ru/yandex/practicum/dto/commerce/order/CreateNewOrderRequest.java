package ru.yandex.practicum.dto.commerce.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.dto.commerce.AddressRequest;

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
