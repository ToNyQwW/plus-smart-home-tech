package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.aop.Loggable;
import ru.yandex.practicum.client.delivery.DeliveryClient;
import ru.yandex.practicum.client.warehouse.WarehouseClient;
import ru.yandex.practicum.dal.entity.Order;
import ru.yandex.practicum.dal.repository.OrderRepository;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;
import ru.yandex.practicum.dto.commerce.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.commerce.order.OrderDto;
import ru.yandex.practicum.dto.commerce.warehouse.BookedProductsDto;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.DeliveryState;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final DeliveryClient deliveryClient;
    private final WarehouseClient warehouseClient;

    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;


    @Override
    @Loggable
    @Transactional
    public OrderDto createOrder(CreateNewOrderRequest request) {
        BookedProductsDto bookedProducts = warehouseClient.checkProductsForShoppingCart(request.getShoppingCart());

        Order order = orderRepository.save(orderMapper.toOrder(request, bookedProducts));

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
}
