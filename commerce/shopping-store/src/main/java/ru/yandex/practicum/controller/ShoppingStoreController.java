package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.ShoppingStoreClient;
import ru.yandex.practicum.dto.commerce.store.CreateProductDto;
import ru.yandex.practicum.dto.commerce.store.ProductDto;
import ru.yandex.practicum.dto.commerce.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.commerce.store.UpdateProductDto;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.model.QuantityState;
import ru.yandex.practicum.service.ShoppingStoreService;
import ru.yandex.practicum.util.PaginationConstants;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController implements ShoppingStoreClient {

    private final ShoppingStoreService shoppingStoreService;

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid CreateProductDto productDto) {
        return shoppingStoreService.createProduct(productDto);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        return shoppingStoreService.getProductById(productId);
    }

    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam ProductCategory category,
                                        @PageableDefault(
                                                size = PaginationConstants.DEFAULT_PAGE_SIZE,
                                                sort = PaginationConstants.DEFAULT_SORT,
                                                direction = ASC) Pageable pageable) {
        return shoppingStoreService.getProductsByCategory(category, pageable);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid UpdateProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping("/quantityState")
    public boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState) {
        SetProductQuantityStateRequest request = SetProductQuantityStateRequest.builder()
                .productId(productId)
                .quantityState(quantityState)
                .build();
        return shoppingStoreService.updateProductQuantityState(request);
    }

    @PostMapping("/removeProductFromStore")
    public boolean deactivateProduct(@RequestBody UUID productId) {
        return shoppingStoreService.deactivateProduct(productId);
    }
}