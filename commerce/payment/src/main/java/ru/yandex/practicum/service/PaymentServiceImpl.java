package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.aop.Loggable;
import ru.yandex.practicum.client.order.OrderClient;
import ru.yandex.practicum.client.store.ShoppingStoreClient;
import ru.yandex.practicum.dal.entity.Payment;
import ru.yandex.practicum.dal.repository.PaymentRepository;
import ru.yandex.practicum.dto.commerce.payment.CalculateProductCostRequest;
import ru.yandex.practicum.dto.commerce.payment.CalculateTotalCostRequest;
import ru.yandex.practicum.dto.commerce.payment.CreatePaymentRequest;
import ru.yandex.practicum.dto.commerce.payment.PaymentDto;
import ru.yandex.practicum.exception.payment.PaymentAlreadyExistsException;
import ru.yandex.practicum.exception.payment.PaymentNotFoundException;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.PaymentState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;

    private static final BigDecimal VAT_RATE = BigDecimal.valueOf(0.1);

    @Override
    @Loggable
    @Transactional
    public PaymentDto createPayment(CreatePaymentRequest request) {
        checkOrderAlreadyHasPayment(request.getOrderId());

        Payment payment = paymentMapper.toPayment(request);

        payment.setFeeTotal(payment.getProductTotal().multiply(VAT_RATE));

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
    @Transactional(readOnly = true)
    public BigDecimal productCost(CalculateProductCostRequest request) {
        Optional<Payment> paymentOptional = getExistingPayment(request.getOrderId());

        if (paymentOptional.isPresent()) {
            return paymentOptional.get().getProductTotal();
        }

        Map<UUID, Long> products = request.getProducts();
        Map<UUID, BigDecimal> productsPrice = shoppingStoreClient.getProductsPrice(products.keySet());

        return productsPrice.entrySet().stream()
                .map(entry -> {
                    Long quantity = products.get(entry.getKey());

                    return entry.getValue().multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Loggable
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalCost(CalculateTotalCostRequest request) {
        Optional<Payment> paymentOptional = getExistingPayment(request.getOrderId());

        if (paymentOptional.isPresent()) {
            return paymentOptional.get().getTotalPayment();
        }

        BigDecimal productPrice = request.getProductPrice();

        return productPrice
                .add(productPrice.multiply(VAT_RATE))
                .add(request.getDeliveryPrice());
    }

    @Override
    @Loggable
    @Transactional
    public PaymentDto paymentSuccess(UUID paymentId) {
        Payment payment = getPaymentOrElseThrow(paymentId);

        payment.setPaymentState(PaymentState.SUCCESS);

        orderClient.paymentSuccess(payment.getOrderId());

        return paymentMapper.toPaymentDto(payment);
    }

    @Override
    @Loggable
    @Transactional
    public PaymentDto paymentFailed(UUID paymentId) {
        Payment payment = getPaymentOrElseThrow(paymentId);

        payment.setPaymentState(PaymentState.FAILED);

        orderClient.paymentFailed(payment.getOrderId());

        return paymentMapper.toPaymentDto(payment);
    }

    private Optional<Payment> getExistingPayment(UUID orderId) {
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);

        paymentOptional.ifPresent(it ->
                log.info("Платеж для заказа с id: {} уже существует. Берем данные из платежа", orderId));

        return paymentOptional;
    }

    private void checkOrderAlreadyHasPayment(UUID orderId) {
        if (paymentRepository.existsByOrderId(orderId)) {
            throw new PaymentAlreadyExistsException("Платеж для заказа " + orderId + " уже существует");
        }
    }

    private Payment getPaymentOrElseThrow(UUID paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() ->
                new PaymentNotFoundException("Платеж с id: " + paymentId + " не найден"));
    }
}
