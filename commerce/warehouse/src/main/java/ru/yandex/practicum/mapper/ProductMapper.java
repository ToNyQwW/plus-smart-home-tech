package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ProductMapper {

    @Mapping(target = "width", source = "dimension.width")
    @Mapping(target = "height", source = "dimension.height")
    @Mapping(target = "depth", source = "dimension.depth")
    Product toProduct(NewProductInWarehouseRequest request);
}