package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.store.CreateProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.UpdateProductDto;

public interface ShoppingStoreService {

    ProductDto createProduct(CreateProductDto productDto);

    ProductDto updateProduct(UpdateProductDto productDto);
}