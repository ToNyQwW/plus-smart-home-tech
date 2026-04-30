package ru.yandex.practicum.client.payment;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.commerce.payment.CalculateProductCostRequest;
import ru.yandex.practicum.dto.commerce.payment.CalculateTotalCostRequest;

import java.math.BigDecimal;

@FeignClient(name = "payment",
        path = "/api/v1/payment",
        configuration = PaymentFeignConfig.class,
        fallbackFactory = PaymentFallbackFactory.class)
public interface PaymentClient {

    @PostMapping("/productCost")
    BigDecimal productCost(@RequestBody @Valid CalculateProductCostRequest request);

    @PostMapping("/totalCost")
    BigDecimal totalCost(@RequestBody @Valid CalculateTotalCostRequest request);
}
