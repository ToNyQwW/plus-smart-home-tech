package ru.yandex.practicum.client.warehouse;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.ShoppingCartRequest;
import ru.yandex.practicum.dto.commerce.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.commerce.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.commerce.warehouse.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse",
        path = "/api/v1/warehouse",
        configuration = WarehouseFeignConfig.class,
        fallbackFactory = WarehouseFallbackFactory.class)
public interface WarehouseClient {

    @PutMapping
    void newProductInWarehouse(@RequestBody @Valid NewProductInWarehouseRequest request);

    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto checkProductsForShoppingCart(@RequestBody @Valid ShoppingCartRequest request);

    @GetMapping("/address")
    AddressDto getAddress();
}