package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dal.entity.ShoppingCart;
import ru.yandex.practicum.dto.commerce.ShoppingCartRequest;
import ru.yandex.practicum.dto.commerce.cart.ShoppingCartDto;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ShoppingCartMapper {

    @Mapping(source = "cartId", target = "shoppingCartId")
    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);

    @Mapping(source = "cartId", target = "shoppingCartId")
    ShoppingCartRequest toShoppingCartRequest(ShoppingCart shoppingCart);

    ShoppingCartDto toShoppingCartDto(ShoppingCartRequest request);
}