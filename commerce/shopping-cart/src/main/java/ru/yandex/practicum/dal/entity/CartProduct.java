package ru.yandex.practicum.dal.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shopping_carts_products")
public class CartProduct {

    @EmbeddedId
    private CartProductId id;

    @ToString.Exclude
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    @ManyToOne(fetch = LAZY)
    private Cart cart;

    @Column(name = "quantity")
    private Long quantity;
}