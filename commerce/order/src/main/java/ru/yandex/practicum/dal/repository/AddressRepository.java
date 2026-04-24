package ru.yandex.practicum.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.entity.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
