package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.dto.commerce.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.commerce.warehouse.BookedProductsDto;

@Value
@Builder
public class OrderCreationContext {

    String username;

    CreateNewOrderRequest request;

    BookedProductsDto warehouseInfo;
}
