package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.ShoppingCartRequest;
import ru.yandex.practicum.dto.commerce.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void createNewProductInWarehouse(NewProductInWarehouseRequest request);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    BookedProductsDto checkProductsForShoppingCart(ShoppingCartRequest request);

    BookedProductsDto assembleProductsForOrder(AssemblyProductsForOrderRequest request);

    void shippedToDelivery(ShippedToDeliveryRequest request);

    void returnProducts(Map<UUID, Long> requestProducts);

    AddressDto getAddress();
}