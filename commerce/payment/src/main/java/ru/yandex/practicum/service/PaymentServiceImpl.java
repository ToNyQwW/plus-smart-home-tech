package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.aop.Loggable;
import ru.yandex.practicum.dal.entity.Payment;
import ru.yandex.practicum.dal.repository.PaymentRepository;
import ru.yandex.practicum.dto.commerce.payment.CalculateTotalCostRequest;
import ru.yandex.practicum.dto.commerce.payment.CreatePaymentRequest;
import ru.yandex.practicum.dto.commerce.payment.PaymentDto;
import ru.yandex.practicum.exception.payment.PaymentAlreadyExistsException;
import ru.yandex.practicum.mapper.PaymentMapper;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    private static final BigDecimal VAT_RATE = BigDecimal.valueOf(0.1);

    @Override
    @Loggable
    @Transactional
    public PaymentDto createPayment(CreatePaymentRequest request) {
        checkOrderAlreadyHasPayment(request.getOrderId());
        checkPaymentIdAlreadyExists(request.getPaymentId());

        Payment payment = paymentMapper.toPayment(request);

        payment.setFeeTotal(request.getProductPrice().multiply(VAT_RATE));

        payment.setTotalPayment(
                payment.getProductTotal()
                        .add(payment.getDeliveryTotal())
                        .add(payment.getFeeTotal())
        );

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toPaymentDto(savedPayment);
    }

    @Override
    @Loggable
    public BigDecimal calculateTotalCost(CalculateTotalCostRequest request) {
        BigDecimal productPrice = request.getProductPrice();

        return productPrice
                .add(productPrice.multiply(VAT_RATE))
                .add(request.getDeliveryPrice());
    }

    private void checkOrderAlreadyHasPayment(UUID orderId) {
        if (paymentRepository.existsByOrderId(orderId)) {
            throw new PaymentAlreadyExistsException("Платеж для заказа " + orderId + " уже существует");
        }
    }

    private void checkPaymentIdAlreadyExists(UUID paymentId) {
        if (paymentRepository.existsById(paymentId)) {
            throw new PaymentAlreadyExistsException("Платеж с id " + paymentId + " уже существует");
        }
    }
}
