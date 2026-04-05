package ru.yandex.practicum.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.dto.store.CreateProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.UpdateProductDto;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = SPRING)
public interface ProductMapper {

    Product toProduct(CreateProductDto productDto);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateProduct(UpdateProductDto productDto, @MappingTarget Product product);

    ProductDto toProductDto(Product product);
}