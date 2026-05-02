package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dal.entity.Address;
import ru.yandex.practicum.dal.entity.Delivery;
import ru.yandex.practicum.dto.commerce.delivery.CreateNewDeliveryRequest;
import ru.yandex.practicum.dto.commerce.delivery.DeliveryDto;
import ru.yandex.practicum.dto.commerce.warehouse.ShippedToDeliveryRequest;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface DeliveryMapper {

    @Mapping(target = "fromAddress", source = "fromAddress")
    @Mapping(target = "toAddress", source = "toAddress")
    Delivery toDelivery(CreateNewDeliveryRequest request, Address fromAddress, Address toAddress);

    DeliveryDto toDeliveryDto(Delivery delivery);

    ShippedToDeliveryRequest toShippedToDeliveryRequest(Delivery delivery);
}
