package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.aop.Loggable;
import ru.yandex.practicum.client.order.OrderClient;
import ru.yandex.practicum.client.warehouse.WarehouseClient;
import ru.yandex.practicum.dal.entity.Address;
import ru.yandex.practicum.dal.entity.Delivery;
import ru.yandex.practicum.dal.repository.AddressRepository;
import ru.yandex.practicum.dal.repository.DeliveryRepository;
import ru.yandex.practicum.dto.commerce.AddressRequest;
import ru.yandex.practicum.dto.commerce.delivery.CalculateDeliveryCostRequest;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;
import ru.yandex.practicum.exception.delivery.DeliveryAlreadyExistsException;
import ru.yandex.practicum.exception.delivery.DeliveryNotFoundException;
import ru.yandex.practicum.exception.delivery.InvalidDeliveryStateException;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.DeliveryState;
import ru.yandex.practicum.pricing.DeliveryCostCalculator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final AddressRepository addressRepository;
    private final DeliveryRepository deliveryRepository;

    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    private final AddressMapper addressMapper;
    private final DeliveryMapper deliveryMapper;

    private final DeliveryCostCalculator deliveryCostCalculator;

    @Override
    @Loggable
    public DeliveryDto createNewDelivery(CreateNewDeliveryRequest request) {
        throwIfDeliveryWithOrderIdExists(request.getOrderId());

        Address toAddress = getOrCreateNewAddress(request.getToAddress());
        Address fromAddress = getOrCreateNewAddress(request.getFromAddress());
        Delivery delivery = deliveryMapper.toDelivery(request, fromAddress, toAddress);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return deliveryMapper.toDeliveryDto(savedDelivery);
    }

    @Override
    @Loggable
    public DeliveryDto completeDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryOrThrowException(deliveryId);

        assertDeliveryState(delivery, DeliveryState.IN_PROGRESS);

        orderClient.deliverySuccessful(delivery.getOrderId());

        delivery.setDeliveryState(DeliveryState.DELIVERED);

        return deliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    @Loggable
    public DeliveryDto startDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryOrThrowException(deliveryId);

        assertDeliveryState(delivery, DeliveryState.CREATED);

        warehouseClient.shippedToDelivery(deliveryMapper.toShippedToDeliveryRequest(delivery));

        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);

        return deliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    @Loggable
    public DeliveryDto failDelivery(UUID deliveryId) {
        Delivery delivery = getDeliveryOrThrowException(deliveryId);

        if (delivery.getDeliveryState() != DeliveryState.FAILED) {
            delivery.setDeliveryState(DeliveryState.FAILED);
        }

        orderClient.deliveryOrderFailed(delivery.getOrderId());

        return deliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    @Loggable
    public BigDecimal calculateDeliveryCost(CalculateDeliveryCostRequest request) {
        Delivery delivery = getDeliveryOrThrowException(request.getDeliveryId());

        return deliveryCostCalculator.calculateDeliveryCost(
                delivery.getFromAddress().getStreet(),
                delivery.getToAddress().getStreet(),
                request.getDeliveryWeight(),
                request.getDeliveryVolume(),
                request.isFragile()
        );
    }

    private Address getOrCreateNewAddress(AddressRequest request) {
        Optional<Address> addressOpt = addressRepository.findByCountryAndCityAndStreetAndHouseAndFlat(
                request.getCountry(),
                request.getCity(),
                request.getStreet(),
                request.getHouse(),
                request.getFlat()
        );

        if (addressOpt.isEmpty()) {
            Address address = addressMapper.toAddress(request);
            return addressRepository.save(address);
        }

        return addressOpt.get();
    }

    private static void assertDeliveryState(Delivery delivery, DeliveryState expectedState) {
        DeliveryState actualState = delivery.getDeliveryState();
        if (actualState != expectedState) {
            throw new InvalidDeliveryStateException(delivery.getDeliveryId(), actualState, expectedState);
        }
    }

    private Delivery getDeliveryOrThrowException(UUID deliveryId) {
        return deliveryRepository.findByDeliveryId(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException("Доставка с id: " + deliveryId + " не найдена"));
    }

    private void throwIfDeliveryWithOrderIdExists(UUID orderId) {
        if (deliveryRepository.existsByOrderId(orderId)) {
            throw new DeliveryAlreadyExistsException("Доставка с id заказа " + orderId + " уже существует");
        }
    }
}
