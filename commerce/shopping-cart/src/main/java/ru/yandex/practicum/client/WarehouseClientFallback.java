package ru.yandex.practicum.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.exception.warehouse.WarehouseServiceUnavailableException;
import ru.yandex.practicum.util.ErrorMessagesConstants;

@Component
@RequiredArgsConstructor
public class WarehouseClientFallback {

    private final WarehouseClient warehouseClient;

    @CircuitBreaker(name = "warehouse", fallbackMethod = "checkProductQuantityFallback")
    public BookedProductsDto checkProductQuantityState(ShoppingCartDto shoppingCart) {
        return warehouseClient.checkProductsForShoppingCart(shoppingCart);
    }

    private BookedProductsDto checkProductQuantityFallback(ShoppingCartDto shoppingCart, Throwable t) {
        throw new WarehouseServiceUnavailableException(ErrorMessagesConstants.WAREHOUSE_SERVICE_UNAVAILABLE);
    }
}