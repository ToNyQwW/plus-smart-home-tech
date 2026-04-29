package ru.yandex.practicum.client.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.ShoppingCartRequest;
import ru.yandex.practicum.dto.commerce.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.commerce.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.commerce.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.exception.OrderNotFoundException;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.warehouse.LowQuantityException;
import ru.yandex.practicum.exception.warehouse.WarehouseServiceUnavailableException;

import java.util.Map;
import java.util.UUID;

@Component
public class WarehouseFallbackFactory implements FallbackFactory<WarehouseClient> {

    @Override
    public WarehouseClient create(Throwable cause) {
        return new WarehouseClient() {

            @Override
            public BookedProductsDto checkProductsForShoppingCart(ShoppingCartRequest request) {
                if (cause instanceof LowQuantityException) {
                    throw (LowQuantityException) cause;
                }
                if (cause instanceof ProductNotFoundException) {
                    throw (ProductNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            @Override
            public BookedProductsDto assembleProductsForOrder(AssemblyProductsForOrderRequest request) {
                if (cause instanceof LowQuantityException) {
                    throw (LowQuantityException) cause;
                }
                if (cause instanceof ProductNotFoundException) {
                    throw (ProductNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            @Override
            public void shippedToDelivery(ShippedToDeliveryRequest request) {
                if (cause instanceof OrderNotFoundException) {
                    throw (OrderNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            @Override
            public void returnProducts(Map<@NotNull UUID, @Positive Long> products) {
                if (cause instanceof ProductNotFoundException) {
                    throw (ProductNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            @Override
            public AddressDto getAddress() {
                throw defaultFallback(cause);
            }

            private WarehouseServiceUnavailableException defaultFallback(Throwable cause) {
                return new WarehouseServiceUnavailableException("Warehouse недоступен: " + cause.getMessage());
            }
        };
    }
}
