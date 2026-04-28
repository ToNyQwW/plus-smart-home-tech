package ru.yandex.practicum.client.warehouse;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.ShoppingCartRequest;
import ru.yandex.practicum.dto.commerce.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.commerce.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.commerce.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.warehouse.LowQuantityException;
import ru.yandex.practicum.exception.warehouse.ProductAlreadyExistsException;
import ru.yandex.practicum.exception.warehouse.WarehouseServiceUnavailableException;

@Component
public class WarehouseFallbackFactory implements FallbackFactory<WarehouseClient> {

    @Override
    public WarehouseClient create(Throwable cause) {
        return new WarehouseClient() {

            @Override
            public void newProductInWarehouse(NewProductInWarehouseRequest request) {
                if (cause instanceof ProductAlreadyExistsException) {
                    throw (ProductAlreadyExistsException) cause;
                }
                throw new WarehouseServiceUnavailableException("Warehouse недоступен: " + cause.getMessage());
            }

            @Override
            public void addProductToWarehouse(AddProductToWarehouseRequest request) {
                if (cause instanceof ProductNotFoundException) {
                    throw (ProductNotFoundException) cause;
                }
                throw new WarehouseServiceUnavailableException("Warehouse недоступен: " + cause.getMessage());
            }

            @Override
            public BookedProductsDto checkProductsForShoppingCart(ShoppingCartRequest request) {
                if (cause instanceof LowQuantityException) {
                    throw (LowQuantityException) cause;
                }
                if (cause instanceof ProductNotFoundException) {
                    throw (ProductNotFoundException) cause;
                }
                throw new WarehouseServiceUnavailableException("Warehouse недоступен: " + cause.getMessage());
            }

            @Override
            public AddressDto getAddress() {
                throw new WarehouseServiceUnavailableException("Warehouse недоступен: " + cause.getMessage());
            }
        };
    }
}
