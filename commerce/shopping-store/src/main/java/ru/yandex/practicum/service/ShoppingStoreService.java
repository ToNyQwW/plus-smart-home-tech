package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.store.CreateProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.store.UpdateProductDto;
import ru.yandex.practicum.model.ProductCategory;

import java.util.UUID;

public interface ShoppingStoreService {

    ProductDto createProduct(CreateProductDto productDto);

    ProductDto getProductById(UUID productId);

    Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    ProductDto updateProduct(UpdateProductDto productDto);

    boolean updateProductQuantityState(SetProductQuantityStateRequest request);

    boolean deactivateProduct(UUID productId);
}