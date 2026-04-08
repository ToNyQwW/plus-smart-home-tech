package ru.yandex.practicum.dal.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.model.ProductState;
import ru.yandex.practicum.model.QuantityState;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shopping_store_products")
public class Product {

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "image_src")
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state")
    private QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state")
    private ProductState productState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    private ProductCategory productCategory;

    @Column(name = "price")
    private Double price;
}