package ru.yandex.practicum.dto.store;

import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.model.ProductState;
import ru.yandex.practicum.model.QuantityState;

import java.util.UUID;

@Value
@Builder
public class ProductDto {

    UUID productId;

    String productName;

    String description;

    String imageSrc;

    QuantityState quantityState;

    ProductState productState;

    ProductCategory productCategory;

    Double price;
}