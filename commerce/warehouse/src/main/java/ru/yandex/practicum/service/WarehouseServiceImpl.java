package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.aop.Loggable;
import ru.yandex.practicum.dal.entity.Order;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.dal.repository.OrderRepository;
import ru.yandex.practicum.dal.repository.ProductRepository;
import ru.yandex.practicum.dto.commerce.AddressDto;
import ru.yandex.practicum.dto.commerce.ShoppingCartRequest;
import ru.yandex.practicum.dto.commerce.warehouse.*;
import ru.yandex.practicum.exception.OrderNotFoundException;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.exception.warehouse.LowQuantityException;
import ru.yandex.practicum.exception.warehouse.ProductAlreadyExistsException;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.WarehouseAddress;

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

    private static final String[] ADDRESSES = new String[]{
            WarehouseAddress.ADDRESS_1.toString(),
            WarehouseAddress.ADDRESS_2.toString()
    };

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    private final OrderRepository orderRepository;
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
    @Loggable
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductsForShoppingCart(ShoppingCartRequest request) {
        return processProducts(request.getProducts(), false);
    }

    @Override
    @Loggable
    @Transactional
    public BookedProductsDto assembleProductsForOrder(AssemblyProductsForOrderRequest request) {
        BookedProductsDto assembledProducts = processProducts(request.getProducts(), true);

        Order order = orderMapper.toOrder(request);
        orderRepository.save(order);

        return assembledProducts;
    }

    @Override
    @Loggable
    @Transactional
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        Order order = getOrderOrElseThrow(request.getOrderId());

        order.setDeliveryId(request.getDeliveryId());
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

    private BookedProductsDto processProducts(Map<UUID, Long> products, boolean shouldDecrease) {
        boolean fragile = false;
        double deliveryVolume = 0;
        double deliveryWeight = 0;
        List<UUID> missingProducts = new ArrayList<>();

        Map<UUID, Product> productMap = getProductsOrElseThrow(products.keySet())
                .stream()
                .collect(toMap(Product::getProductId, Function.identity()));

        for (var entry : products.entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();

            Product product = productMap.get(productId);

            if (product.getQuantity() < requestedQuantity) {
                missingProducts.add(productId);
            }
        }

        if (!missingProducts.isEmpty()) {
            throw new LowQuantityException("Недостаточно товаров на складе с Id: " + missingProducts);
        }

        for (var entry : products.entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();

            Product product = productMap.get(productId);

            if (shouldDecrease) {
                product.setQuantity(product.getQuantity() - requestedQuantity);
            }

            deliveryVolume += product.getVolume() * requestedQuantity;
            deliveryWeight += product.getWeight() * requestedQuantity;

            if (product.isFragile()) {
                fragile = true;
            }
        }

        return BookedProductsDto.builder()
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();
    }

    private Order getOrderOrElseThrow(UUID orderId) {
        return orderRepository.findByOrderId(orderId).orElseThrow(() ->
                new OrderNotFoundException("Заказ с id: " + orderId + " не найден"));
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