package ru.yandex.practicum.dal.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.model.ShoppingCartState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shopping_carts")
public class Cart {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;

    @Column(name = "username")
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(name = "cart_state")
    private ShoppingCartState cartState;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "cart", cascade = ALL, orphanRemoval = true, fetch = EAGER)
    private List<CartProduct> products = new ArrayList<>();
}