package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;
import ru.yandex.practicum.service.DeliveryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto createNewDelivery(@RequestBody CreateNewDeliveryRequest request) {
        return deliveryService.createNewDelivery(request);
    }
}
