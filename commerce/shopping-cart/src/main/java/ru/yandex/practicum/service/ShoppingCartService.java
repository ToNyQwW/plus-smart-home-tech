package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.ShoppingCartDto;

import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProducts(String username, Map<UUID, Long> products);

    void deactivateShoppingCart(String username);
}