package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.warehouse.WarehouseClient;
import ru.yandex.practicum.dto.commerce.ShoppingCartRequest;
import ru.yandex.practicum.dto.commerce.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.commerce.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.commerce.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;

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

    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }
}