package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.aop.Loggable;
import ru.yandex.practicum.dal.entity.Payment;
import ru.yandex.practicum.dal.repository.PaymentRepository;
import ru.yandex.practicum.dto.commerce.OrderRequest;
import ru.yandex.practicum.dto.commerce.payment.PaymentDto;
import ru.yandex.practicum.exception.payment.PaymentAlreadyExistsException;
import ru.yandex.practicum.mapper.PaymentMapper;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    private static final BigDecimal VAT_RATE = BigDecimal.valueOf(0.1);

    @Override
    @Loggable
    public PaymentDto createPayment(OrderRequest request) {
        throwIfPaymentExists(request.getPaymentId());

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

    private void throwIfPaymentExists(UUID paymentId) {
        if (paymentRepository.existsById(paymentId)) {
            throw new PaymentAlreadyExistsException("Платеж с id  " + paymentId + " уже существует");
        }
    }
}
