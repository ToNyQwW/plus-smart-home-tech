package ru.yandex.practicum.dal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouse_products")
public class Product {

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "fragile")
    private boolean fragile;

    @Column(name = "width")
    private double width;

    @Column(name = "height")
    private double height;

    @Column(name = "depth")
    private double depth;

    @Column(name = "weight")
    private double weight;

    @Column(name = "quantity")
    private long quantity;
}