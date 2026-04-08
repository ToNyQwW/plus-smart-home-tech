package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    void createNewProductInWarehouse(NewProductInWarehouseRequest request);
}