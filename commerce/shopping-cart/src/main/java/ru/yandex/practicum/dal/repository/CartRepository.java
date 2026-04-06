package ru.yandex.practicum.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.entity.Cart;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}