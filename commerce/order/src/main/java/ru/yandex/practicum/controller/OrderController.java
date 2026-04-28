package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.commerce.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.commerce.order.OrderDto;
import ru.yandex.practicum.service.OrderService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PutMapping()
    public OrderDto createOrder(@RequestParam @NotBlank String username,
                                @RequestBody @Valid CreateNewOrderRequest request) {
        return orderService.createOrder(username, request);
    }
}
