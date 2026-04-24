package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.commerce.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProducts(String username, Map<UUID, Long> products);

    ShoppingCartDto removeProducts(String username, List<UUID> products);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);

    void deactivateShoppingCart(String username);
}