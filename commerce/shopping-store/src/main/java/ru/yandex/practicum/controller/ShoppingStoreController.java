package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.service.ShoppingStoreService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController {

    private final ShoppingStoreService shoppingStoreService;

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        return shoppingStoreService.createProduct(productDto);
    }
}