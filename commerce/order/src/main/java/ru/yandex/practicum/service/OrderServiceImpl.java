package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.aop.Loggable;
import ru.yandex.practicum.client.delivery.DeliveryClient;
import ru.yandex.practicum.client.payment.PaymentClient;
import ru.yandex.practicum.client.warehouse.WarehouseClient;
import ru.yandex.practicum.dal.entity.Order;
import ru.yandex.practicum.dal.repository.OrderRepository;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;
import ru.yandex.practicum.dto.commerce.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.commerce.order.OrderDto;
import ru.yandex.practicum.dto.commerce.order.ProductReturnRequest;
import ru.yandex.practicum.dto.commerce.warehouse.BookedProductsDto;
import ru.yandex.practicum.exception.OrderNotFoundException;
import ru.yandex.practicum.exception.order.InvalidOrderStateException;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.DeliveryState;
import ru.yandex.practicum.model.OrderCreationContext;
import ru.yandex.practicum.model.OrderState;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static ru.yandex.practicum.model.OrderState.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;
    private final WarehouseClient warehouseClient;

    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;


    @Override
    @Loggable
    @Transactional
    public OrderDto createOrder(String username, CreateNewOrderRequest request) {
        BookedProductsDto bookedProducts = warehouseClient.checkProductsForShoppingCart(request.getShoppingCart());

        OrderCreationContext orderCreationContext = OrderCreationContext.builder()
                .request(request)
                .warehouseInfo(bookedProducts)
                .username(username)
                .build();

        Order order = orderRepository.save(orderMapper.toOrder(orderCreationContext));

        AddressDto warehouseAddress = warehouseClient.getAddress();

        CreateNewDeliveryRequest deliveryRequest = CreateNewDeliveryRequest.builder()
                .fromAddress(addressMapper.toAddressRequest(warehouseAddress))
                .toAddress(request.getDeliveryAddress())
                .orderId(order.getOrderId())
                .deliveryState(DeliveryState.CREATED)
                .build();

        DeliveryDto delivery = deliveryClient.createNewDelivery(deliveryRequest);

        order.setDeliveryId(delivery.getDeliveryId());
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Loggable
    @Transactional
    public OrderDto calculateDeliveryPrice(UUID orderId) {
        Order order = getOrderOrElseThrow(orderId);

        BigDecimal deliveryPrice = deliveryClient.deliveryCost(orderMapper.toCalculateDeliveryCostRequest(order));
        order.setDeliveryPrice(deliveryPrice);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Loggable
    @Transactional
    public OrderDto calculateTotalPrice(UUID orderId) {
        Order order = getOrderOrElseThrow(orderId);

        BigDecimal productPrice = paymentClient.productCost(orderMapper.toCalculateProductCostRequest(order));
        order.setProductPrice(productPrice);

        BigDecimal totalPrice = paymentClient.totalCost(orderMapper.toCalculateTotalCostRequest(order));
        order.setTotalPrice(totalPrice);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Loggable
    @Transactional
    public OrderDto assemblyOrder(UUID orderId) {
        Order order = getOrderOrElseThrow(orderId);

        BookedProductsDto bookedProducts = warehouseClient.assembleProductsForOrder(
                orderMapper.toAssemblyProductsForOrderRequest(order)
        );

        order.setFragile(bookedProducts.isFragile());
        order.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        order.setDeliveryWeight(bookedProducts.getDeliveryWeight());
        order.setState(ASSEMBLED);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Loggable
    @Transactional
    public OrderDto assemblyOrderFailed(UUID orderId) {
        Order order = getOrderOrElseThrow(orderId);

        order.setState(ASSEMBLY_FAILED);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Loggable
    @Transactional
    public OrderDto returnOrder(ProductReturnRequest request) {
        UUID orderId = request.getOrderId();
        Order order = getOrderOrElseThrow(orderId);

        assertOrderState(orderId, order.getState());

        warehouseClient.returnProducts(request.getProducts());

        order.setState(PRODUCT_RETURNED);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Loggable
    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrders(String username) {
        List<Order> orders = orderRepository.getOrdersByUsername(username);

        return orders.stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    private void assertOrderState(UUID orderId, OrderState state) {
        if (state == CANCELED || state == PRODUCT_RETURNED) {
            throw new InvalidOrderStateException(orderId, state);
        }
    }

    private Order getOrderOrElseThrow(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new OrderNotFoundException("Заказ с id: " + orderId + " не найден"));
    }
}
