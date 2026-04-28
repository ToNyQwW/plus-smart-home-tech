package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.client.delivery.DeliveryClient;
import ru.yandex.practicum.dto.commerce.delivery.CalculateDeliveryCostRequest;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;
import ru.yandex.practicum.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/delivery")
public class DeliveryController implements DeliveryClient {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto createNewDelivery(@RequestBody @Valid CreateNewDeliveryRequest request) {
        return deliveryService.createNewDelivery(request);
    }

    @PostMapping("/successful")
    public DeliveryDto successfulDelivery(@RequestBody UUID orderId) {
        return deliveryService.completeDelivery(orderId);
    }

    @PostMapping("/picked")
    public DeliveryDto pickedDelivery(@RequestBody UUID orderId) {
        return deliveryService.startDelivery(orderId);
    }

    @PostMapping("/failed")
    public DeliveryDto failedDelivery(@RequestBody UUID orderId) {
        return deliveryService.failDelivery(orderId);
    }

    @PostMapping("/cost")
    public BigDecimal deliveryCost(@RequestBody @Valid CalculateDeliveryCostRequest request) {
        return deliveryService.calculateDeliveryCost(request);
    }
}
