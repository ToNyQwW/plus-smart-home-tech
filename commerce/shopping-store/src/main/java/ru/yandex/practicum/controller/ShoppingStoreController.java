package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.store.CreateProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.store.UpdateProductDto;
import ru.yandex.practicum.service.ShoppingStoreService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController {

    private final ShoppingStoreService shoppingStoreService;

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        return shoppingStoreService.getProductById(productId);
    }

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid CreateProductDto productDto) {
        return shoppingStoreService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid UpdateProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean deactivateProduct(@RequestBody UUID productId) {
        return shoppingStoreService.deactivateProduct(productId);
    }

    @PostMapping("/quantityState")
    public boolean setProductQuantityState(@RequestBody @Valid SetProductQuantityStateRequest request) {
        return shoppingStoreService.updateProductQuantityState(request);
    }
}