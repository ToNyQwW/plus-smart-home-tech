package ru.yandex.practicum.client.store;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.store.ShoppingStoreServiceUnavailableException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class ShoppingStoreFallbackFactory implements FallbackFactory<ShoppingStoreClient> {
    @Override
    public ShoppingStoreClient create(Throwable cause) {
        return new ShoppingStoreClient() {

            @Override
            public Map<UUID, BigDecimal> getProductsPrice(Set<UUID> productIds) {
                if(cause instanceof ProductNotFoundException){
                    throw (ProductNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            private ShoppingStoreServiceUnavailableException defaultFallback(Throwable cause) {
                return new ShoppingStoreServiceUnavailableException("Shopping store недоступен: " + cause.getMessage());
            }
        };
    }
}
