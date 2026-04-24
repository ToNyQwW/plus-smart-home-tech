package ru.yandex.practicum.dal.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.model.OrderState;

import java.math.BigDecimal;
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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id")
    private Address address;

    @Builder.Default
    @ToString.Exclude
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products = new HashMap<>();

    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @Column(name = "payment_id", unique = true)
    private UUID paymentId;

    @Column(name = "delivery_id", unique = true)
    private UUID deliveryId;

    @Column(name = "delivery_weight")
    private double deliveryWeight;

    @Column(name = "delivery_volume")
    private double deliveryVolume;

    @Column(name = "delivery_price")
    private BigDecimal deliveryPrice;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private String username;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderState state =  OrderState.NEW;

    private boolean fragile;
}
