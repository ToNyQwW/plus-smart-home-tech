package ru.yandex.practicum.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CartProductId {

    @Column(name = "cart_id")
    private UUID cartId;

    @Column(name = "product_id")
    private UUID productId;
}