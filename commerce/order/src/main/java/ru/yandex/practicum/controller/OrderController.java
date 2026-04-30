package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.commerce.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.commerce.order.OrderDto;
import ru.yandex.practicum.dto.commerce.order.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

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

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryPrice(@RequestBody UUID orderId) {
        return orderService.calculateDeliveryPrice(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotalPrice(@RequestBody UUID orderId) {
        return orderService.calculateTotalPrice(orderId);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest request) {
        return orderService.returnOrder(request);
    }

    //TODO: добавить пагинацию
    @GetMapping
    public List<OrderDto> getUserOrders(@RequestParam @NotBlank String username) {
        return orderService.getUserOrders(username);
    }
}
