package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.repository.AddressRepository;
import ru.yandex.practicum.dal.repository.OrderRepository;
import ru.yandex.practicum.dto.commerce.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.commerce.order.OrderDto;
import ru.yandex.practicum.mapper.AddressMapper;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final AddressMapper addressMapper;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;


    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        return null;
    }
}
