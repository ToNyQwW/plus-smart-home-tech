package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dal.entity.Order;
import ru.yandex.practicum.dto.commerce.warehouse.AssemblyProductsForOrderRequest;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OrderMapper {

    Order toOrder (AssemblyProductsForOrderRequest request);
}
