package ru.yandex.practicum.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.store.CreateProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.UpdateProductDto;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.model.QuantityState;
import ru.yandex.practicum.util.PaginationConstants;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.ASC;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @PutMapping
    ProductDto createProduct(@RequestBody @Valid CreateProductDto productDto);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);

    @GetMapping
    Page<ProductDto> getProducts(@RequestParam ProductCategory category,
                                 @PageableDefault(
                                         size = PaginationConstants.DEFAULT_PAGE_SIZE,
                                         sort = PaginationConstants.DEFAULT_SORT,
                                         direction = ASC) Pageable pageable);

    @PostMapping
    ProductDto updateProduct(@RequestBody @Valid UpdateProductDto productDto);

    @PostMapping("/quantityState")
    boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState);

    @PostMapping("/removeProductFromStore")
    boolean deactivateProduct(@RequestBody UUID productId);
}