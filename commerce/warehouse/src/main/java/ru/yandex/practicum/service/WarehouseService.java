package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.commerce.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.commerce.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    void createNewProductInWarehouse(NewProductInWarehouseRequest request);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    BookedProductsDto checkProductsForShoppingCart(ShoppingCartDto request);

    AddressDto getAddress();
}