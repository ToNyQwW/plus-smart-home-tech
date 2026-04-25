package ru.yandex.practicum.dal.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.entity.Delivery;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    boolean existsByOrderId(UUID orderId);

    @EntityGraph(attributePaths = {"fromAddress", "toAddress"})
    Optional<Delivery> findByOrderId(UUID orderId);
}
