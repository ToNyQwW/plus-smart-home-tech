package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dal.entity.Order;
import ru.yandex.practicum.dto.commerce.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.commerce.order.OrderDto;
import ru.yandex.practicum.dto.commerce.warehouse.BookedProductsDto;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    @Mapping(target = "username", source = "request.username")
    @Mapping(target = "products", source = "request.shoppingCart.products")
    @Mapping(target = "shoppingCartId", source = "request.shoppingCart.shoppingCartId")
    @Mapping(target = "fragile", source = "dto.fragile")
    @Mapping(target = "deliveryVolume", source = "dto.deliveryVolume")
    @Mapping(target = "deliveryWeight", source = "dto.deliveryWeight")
    Order toOrder(CreateNewOrderRequest request, BookedProductsDto dto);
}
