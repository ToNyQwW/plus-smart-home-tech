package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.store.CreateProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.UpdateProductDto;

import java.util.UUID;

public interface ShoppingStoreService {

    ProductDto createProduct(CreateProductDto productDto);

    ProductDto updateProduct(UpdateProductDto productDto);

    boolean deactivateProduct(UUID productId);
}