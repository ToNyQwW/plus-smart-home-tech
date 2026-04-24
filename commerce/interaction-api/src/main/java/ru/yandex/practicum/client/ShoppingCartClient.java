package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.commerce.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.commerce.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient {

    @PutMapping
    ShoppingCartDto addProductsToShoppingCart(@RequestParam String username,
                                              @RequestBody @NotEmpty Map<UUID, Long> products);

    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto removeFromShoppingCart(@RequestParam String username, @RequestBody @NotEmpty List<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantity(@RequestParam String username, @RequestBody @Valid ChangeProductQuantityRequest request);

    @DeleteMapping
    void deactivateShoppingCart(@RequestParam String username);
}