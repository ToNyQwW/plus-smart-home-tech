package ru.yandex.practicum.dal.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.model.ShoppingCartState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shopping_carts")
public class ShoppingCart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;

    @Column(name = "username")
    private String userName;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "cart_state")
    private ShoppingCartState cartState = ShoppingCartState.OPENED;

    @Builder.Default
    @Column(name = "quantity")
    @MapKeyColumn(name = "product_id")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "shopping_carts_products", joinColumns = @JoinColumn(name = "cart_id"))
    private Map<UUID, Long> products = new HashMap<>();

    @Transient
    public void addProducts(Map<UUID, Long> products) {
        products.forEach((id, quantity)
                -> this.products.merge(id, quantity, Long::sum));
    }
}