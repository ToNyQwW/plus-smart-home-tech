package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.commerce.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.commerce.cart.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PutMapping
    public ShoppingCartDto addProductsToShoppingCart(@RequestParam String username,
                                                     @RequestBody @NotEmpty Map<UUID, Long> products) {
        return shoppingCartService.addProducts(username, products);
    }

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeFromShoppingCart(@RequestParam String username,
                                                  @RequestBody @NotEmpty List<UUID> products) {
        return shoppingCartService.removeProducts(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam String username,
                                                 @RequestBody @Valid ChangeProductQuantityRequest request) {
        return shoppingCartService.changeProductQuantity(username, request);
    }

    @DeleteMapping
    public void deactivateShoppingCart(@RequestParam String username) {
        shoppingCartService.deactivateShoppingCart(username);
    }
}