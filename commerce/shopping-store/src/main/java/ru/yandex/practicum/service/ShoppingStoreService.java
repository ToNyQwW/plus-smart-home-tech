package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CreateProductDto;
import ru.yandex.practicum.dto.ProductDto;

public interface ShoppingStoreService {

    ProductDto createProduct(CreateProductDto productDto);
}