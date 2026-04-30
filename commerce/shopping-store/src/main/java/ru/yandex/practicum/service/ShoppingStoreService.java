package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.commerce.store.CreateProductDto;
import ru.yandex.practicum.dto.commerce.store.ProductDto;
import ru.yandex.practicum.dto.commerce.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.commerce.store.UpdateProductDto;
import ru.yandex.practicum.model.ProductCategory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ShoppingStoreService {

    ProductDto createProduct(CreateProductDto productDto);

    ProductDto getProductById(UUID productId);

    Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable);

    Map<UUID, BigDecimal> getProductsPrice(Set<UUID> productIds);

    ProductDto updateProduct(UpdateProductDto productDto);

    boolean updateProductQuantityState(SetProductQuantityStateRequest request);

    boolean deactivateProduct(UUID productId);
}