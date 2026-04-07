package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProducts(String username, Map<UUID, Long> products);

    ShoppingCartDto removeProducts(String username, List<UUID> products);

    void deactivateShoppingCart(String username);
}