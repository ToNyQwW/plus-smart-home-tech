package ru.yandex.practicum.client.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.ShoppingCartRequest;
import ru.yandex.practicum.dto.commerce.warehouse.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse",
        path = "/api/v1/warehouse",
        configuration = WarehouseFeignConfig.class,
        fallbackFactory = WarehouseFallbackFactory.class)
public interface WarehouseClient {

    @PutMapping
    void newProductInWarehouse(@RequestBody @Valid NewProductInWarehouseRequest request);

    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto checkProductsForShoppingCart(@RequestBody @Valid ShoppingCartRequest request);

    @GetMapping("/address")
    AddressDto getAddress();

    @PostMapping("/assembly")
    BookedProductsDto assembleProductsForOrder(@RequestBody @Valid AssemblyProductsForOrderRequest request);

    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void returnProducts(@RequestBody @Valid @NotEmpty Map<@NotNull UUID, @Positive Long> products);
}