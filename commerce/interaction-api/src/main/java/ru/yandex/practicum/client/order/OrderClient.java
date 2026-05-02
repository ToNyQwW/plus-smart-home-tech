package ru.yandex.practicum.client.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.commerce.order.OrderDto;

import java.util.UUID;

@FeignClient(name = "order",
        path = "/api/v1/order",
        configuration = OrderFeignConfig.class,
        fallbackFactory = OrderFallbackFactory.class)
public interface OrderClient {

    @PostMapping("/payment")
    OrderDto paymentSuccess(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliverySuccessful(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliveryOrderFailed(@RequestBody UUID orderId);
}
