package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.dto.CreateProductDto;
import ru.yandex.practicum.dto.ProductDto;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ProductMapper {

    Product toProduct(CreateProductDto productDto);

    ProductDto toProductDto(Product product);
}