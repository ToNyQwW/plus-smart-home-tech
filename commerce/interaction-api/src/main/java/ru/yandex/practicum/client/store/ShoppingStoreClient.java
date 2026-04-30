package ru.yandex.practicum.client.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "shopping-store",
        path = "/api/v1/shopping-store",
        configuration = ShoppingStoreFeignConfig.class,
        fallback = ShoppingStoreFeignErrorDecoder.class)
public interface ShoppingStoreClient {

    @GetMapping("/productsPrice")
    Map<UUID, BigDecimal> getProductsPrice(@RequestBody @Valid @NotEmpty Set<UUID> productIds);
}