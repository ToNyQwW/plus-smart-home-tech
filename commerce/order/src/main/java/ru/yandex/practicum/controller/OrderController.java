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

    @PostMapping("/payment")
    public OrderDto paymentSuccess(@RequestBody UUID orderId) {
        return orderService.paymentSuccess(orderId);
    }

    @PostMapping("/payment")
    public OrderDto paymentFailed(@RequestBody UUID orderId) {
        return orderService.paymentFailed(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody UUID orderId) {
        return orderService.assemblyOrder(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyOrderFailed(@RequestBody UUID orderId) {
        return orderService.assemblyOrderFailed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto deliverySuccessful(@RequestBody UUID orderId) {
        return orderService.deliverySuccessful(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliveryOrderFailed(@RequestBody UUID orderId) {
        return orderService.deliveryOrderFailed(orderId);
    }

    @PostMapping("//completed")
    public OrderDto completedOrder(@RequestBody UUID orderId) {
        return orderService.completedOrder(orderId);
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
