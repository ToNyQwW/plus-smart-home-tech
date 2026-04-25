package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto createNewDelivery(@RequestBody CreateNewDeliveryRequest request) {
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
}
