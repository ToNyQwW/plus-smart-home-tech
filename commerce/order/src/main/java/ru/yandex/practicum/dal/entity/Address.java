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
@Table(name = "address")
public class Address {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    private String country;

    private String city;

    private String street;

    private String house;

    private String flat;
}
