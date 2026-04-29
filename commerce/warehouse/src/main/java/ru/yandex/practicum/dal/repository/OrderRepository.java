package ru.yandex.practicum.dal.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.entity.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @EntityGraph(attributePaths = {"products"})
    Optional<Order> findByOrderId(UUID orderId);
}
