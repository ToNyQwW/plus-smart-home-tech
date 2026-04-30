package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dal.entity.Order;
import ru.yandex.practicum.dto.commerce.delivery.CalculateDeliveryCostRequest;
import ru.yandex.practicum.dto.commerce.order.OrderDto;
import ru.yandex.practicum.model.OrderCreationContext;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    @Mapping(target = "products", source = "request.shoppingCart.products")
    @Mapping(target = "shoppingCartId", source = "request.shoppingCart.shoppingCartId")
    @Mapping(target = "fragile", source = "warehouseInfo.fragile")
    @Mapping(target = "deliveryVolume", source = "warehouseInfo.deliveryVolume")
    @Mapping(target = "deliveryWeight", source = "warehouseInfo.deliveryWeight")
    Order toOrder(OrderCreationContext context);

    CalculateDeliveryCostRequest toCalculateDeliveryCostRequest(Order order);
}
