package ru.yandex.practicum.client.delivery;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.commerce.delivery.CalculateDeliveryCostRequest;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "delivery",
        path = "/api/v1/delivery",
        configuration = DeliveryFeignConfig.class,
        fallbackFactory = DeliveryFallbackFactory.class)
public interface DeliveryClient {

    @PutMapping
    DeliveryDto createNewDelivery(@RequestBody @Valid CreateNewDeliveryRequest request);

    @PostMapping("/successful")
    DeliveryDto successfulDelivery(@RequestBody UUID orderId);

    @PostMapping("/picked")
    DeliveryDto pickedDelivery(@RequestBody UUID orderId);

    @PostMapping("/failed")
    DeliveryDto failedDelivery(@RequestBody UUID orderId);

    @PostMapping("/cost")
    BigDecimal deliveryCost(@RequestBody @Valid CalculateDeliveryCostRequest request);
}
