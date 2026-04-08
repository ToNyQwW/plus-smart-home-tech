package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.dal.repository.ProductRepository;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.warehouse.ProductAlreadyExistsException;
import ru.yandex.practicum.mapper.ProductMapper;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Override
    public void createNewProductInWarehouse(NewProductInWarehouseRequest request) {
        log.info("Метод createNewProductInWarehouse. request: {}", request);
        throwIfProductAlreadyExists(request.getProductId());

        Product product = productMapper.toProduct(request);
        productRepository.save(product);
        log.info("Товар добавлен на склад: {}", product);
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        log.info("Метод addProductToWarehouse. request: {}", request);
        UUID productId = request.getProductId();
        Product product = getProductOrElseThrow(productId);

        long oldQuantity = product.getQuantity();
        product.setQuantity(oldQuantity + request.getQuantity());
        log.info("Количество товара c id: {} изменено с {} на {}", productId, oldQuantity, product.getQuantity());
    }

    @Override
    @Transient
    public AddressDto getAddress() {
        log.info("Метод getAddress. возвращаемый адрес: {}", CURRENT_ADDRESS);
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    private void throwIfProductAlreadyExists(UUID productId) {
        if (productRepository.existsById(productId)) {
            log.info("Товар с id: {} уже зарегистрирован на складе", productId);
            throw new ProductAlreadyExistsException("Товар с id: " + productId + " уже зарегистрирован на складе");
        }
    }

    private Product getProductOrElseThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                            log.info("Товар с Id: {} не найден", productId);
                            return new ProductNotFoundException("Товар с Id: " + productId + " не найден");
                        }
                );
    }
}