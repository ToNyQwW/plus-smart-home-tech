package ru.yandex.practicum.dal.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.model.DeliveryState;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery")
public class Delivery {

    @Id
    @Column(name = "delivery_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_address_id")
    private Address fromAddress;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_address_id")
    private Address toAddress;

    @Column(name = "order_id")
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state")
    private DeliveryState deliveryState;
}
