package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.store.CreateProductDto;
import ru.yandex.practicum.dto.store.ProductDto;

public interface ShoppingStoreService {

    ProductDto createProduct(CreateProductDto productDto);
}