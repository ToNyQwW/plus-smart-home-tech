package ru.yandex.practicum.service;

import jakarta.validation.Valid;
import ru.yandex.practicum.dto.store.CreateProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.store.UpdateProductDto;

import java.util.UUID;

public interface ShoppingStoreService {

    ProductDto createProduct(CreateProductDto productDto);

    ProductDto updateProduct(UpdateProductDto productDto);

    boolean deactivateProduct(UUID productId);

    boolean updateProductQuantityState(SetProductQuantityStateRequest request);
}