package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.dal.repository.ProductRepository;
import ru.yandex.practicum.dto.commerce.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.commerce.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.commerce.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.warehouse.LowQuantityException;
import ru.yandex.practicum.exception.warehouse.ProductAlreadyExistsException;
import ru.yandex.practicum.mapper.ProductMapper;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

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
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductsForShoppingCart(ShoppingCartDto request) {
        log.info("Метод checkProductsForShoppingCart. request: {}", request);
        boolean fragile = false;
        double deliveryVolume = 0;
        double deliveryWeight = 0;
        List<UUID> missingProducts = new ArrayList<>();

        Map<UUID, Long> requestProducts = request.getProducts();
        Map<UUID, Product> productMap = getProductsOrElseThrow(requestProducts.keySet())
                .stream()
                .collect(toMap(Product::getProductId, Function.identity()));

        for (var productEntry : requestProducts.entrySet()) {
            UUID productId = productEntry.getKey();
            long requestedQuantity = productEntry.getValue();

            Product product = productMap.get(productId);

            long availableQuantity = product.getQuantity();
            if (availableQuantity < requestedQuantity) {
                log.info("Недостаточное количество на складе для товара с id: {}. Нужно {}, есть {}",
                        productId, requestedQuantity, availableQuantity);
                missingProducts.add(productId);
                continue;
            }

            deliveryVolume += product.getVolume() * requestedQuantity;
            deliveryWeight += product.getWeight() * requestedQuantity;

            if (product.isFragile()) {
                fragile = true;
            }
        }

        if (!missingProducts.isEmpty()) {
            log.info("Недостаток товаров на складе для корзины c ID {}:  список товаров : {}",
                    request.getShoppingCartId(), missingProducts);
            throw new LowQuantityException("Недостаточно товаров на складе для товаров с Id: " + missingProducts);
        }

        log.info("Количество товара на складе достаточно. Общий вес: {}, объем {}, хрупкий товар: {}",
                deliveryWeight, deliveryVolume, fragile);
        return BookedProductsDto.builder()
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();
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
        return productRepository.findById(productId).orElseThrow(() -> {
            log.info("Товар с Id: {} не найден", productId);
            return new ProductNotFoundException("Товар с Id: " + productId + " не найден");
        });
    }

    private List<Product> getProductsOrElseThrow(Set<UUID> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            Set<UUID> foundIds = products.stream()
                    .map(Product::getProductId)
                    .collect(toSet());

            List<UUID> missingIds = productIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            log.info("Товары c Id не найдены: {}", missingIds);

            throw new ProductNotFoundException("Товары с Id не найдены: " + missingIds);
        }

        return products;
    }
}