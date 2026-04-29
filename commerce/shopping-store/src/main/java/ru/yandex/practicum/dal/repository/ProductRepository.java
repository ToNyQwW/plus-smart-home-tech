package ru.yandex.practicum.dal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.model.ProductCategory;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findByProductCategory(ProductCategory productCategory, Pageable pageable);

    List<Product> findByProductIdIn(Set<UUID> productIds);
}