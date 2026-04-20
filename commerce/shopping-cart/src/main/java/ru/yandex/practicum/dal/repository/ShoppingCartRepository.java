package ru.yandex.practicum.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.entity.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartState;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    Optional<ShoppingCart> findByUserName(String username);

    Optional<ShoppingCart> findByUserNameAndCartState(String username, ShoppingCartState state);
}