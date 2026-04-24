package ru.yandex.practicum.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.entity.Delivery;

import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

}
