package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.dal.repository.ProductRepository;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.exception.warehouse.ProductAlreadyExistsException;
import ru.yandex.practicum.mapper.ProductMapper;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public void createNewProductInWarehouse(NewProductInWarehouseRequest request) {
        log.info("Метод createNewProductInWarehouse. request: {}", request);
        throwIfProductAlreadyExists(request.getProductId());

        Product product = productMapper.toProduct(request);
        productRepository.save(product);
        log.info("Товар добавлен на склад: {}", product);
    }

    private void throwIfProductAlreadyExists(UUID productId) {
        if (productRepository.existsById(productId)) {
            log.info("Товар с id: {} уже зарегистрирован на складе", productId);
            throw new ProductAlreadyExistsException("Товар с id: " + productId + " уже зарегистрирован на складе");
        }
    }
}