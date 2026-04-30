package ru.yandex.practicum.client.payment;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.commerce.payment.CalculateProductCostRequest;
import ru.yandex.practicum.dto.commerce.payment.CalculateTotalCostRequest;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.payment.PaymentServiceUnavailableException;

import java.math.BigDecimal;

@Component
public class PaymentFallbackFactory implements FallbackFactory<PaymentClient> {

    @Override
    public PaymentClient create(Throwable cause) {
        return new PaymentClient() {

            @Override
            public BigDecimal productCost(CalculateProductCostRequest request) {
                if(cause instanceof ProductNotFoundException){
                    throw (ProductNotFoundException) cause;
                }
                throw defaultFallback(cause);
            }

            @Override
            public BigDecimal totalCost(CalculateTotalCostRequest request) {
                throw defaultFallback(cause);
            }

            private PaymentServiceUnavailableException defaultFallback(Throwable cause) {
                return new PaymentServiceUnavailableException("Payment недоступен: " + cause.getMessage());
            }
        };
    }
}
