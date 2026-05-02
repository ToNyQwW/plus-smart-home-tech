package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.warehouse.WarehouseClient;
import ru.yandex.practicum.dto.commerce.ShoppingCartRequest;
import ru.yandex.practicum.dto.commerce.warehouse.*;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseClient {

    private final WarehouseService warehouseService;

    @PutMapping
    public void newProductInWarehouse(@RequestBody @Valid NewProductInWarehouseRequest request) {
        warehouseService.createNewProductInWarehouse(request);
    }

    @PostMapping("/add")
    public void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request) {
        warehouseService.addProductToWarehouse(request);
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductsForShoppingCart(@RequestBody @Valid ShoppingCartRequest request) {
        return warehouseService.checkProductsForShoppingCart(request);
    }

    @PostMapping("/assembly")
    public BookedProductsDto assembleProductsForOrder(@RequestBody @Valid AssemblyProductsForOrderRequest request) {
        return warehouseService.assembleProductsForOrder(request);
    }

    @PostMapping("/shipped")
    public void shippedToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request) {
        warehouseService.shippedToDelivery(request);
    }

    @PostMapping("/return")
    public void returnProducts(@RequestBody @Valid @NotEmpty Map<@NotNull UUID, @Positive Long> products) {
        warehouseService.returnProducts(products);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }
}