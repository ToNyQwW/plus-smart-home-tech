package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dal.entity.Address;
import ru.yandex.practicum.dto.order.AddressRequest;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AddressMapper {

    @Mapping(target = "addressId", ignore = true)
    Address toAddress(AddressRequest request);
}
