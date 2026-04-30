package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.aop.Loggable;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.dal.repository.ProductRepository;
import ru.yandex.practicum.dto.commerce.store.CreateProductDto;
import ru.yandex.practicum.dto.commerce.store.ProductDto;
import ru.yandex.practicum.dto.commerce.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.commerce.store.UpdateProductDto;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.ProductCategory;
import ru.yandex.practicum.model.ProductState;
import ru.yandex.practicum.model.QuantityState;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public ProductDto createProduct(CreateProductDto productDto) {
        log.info("Метод createProduct. DTO: {}", productDto);
        Product product = productMapper.toProduct(productDto);
        Product savedProduct = productRepository.save(product);
        log.info("Товар сохранен в БД с ID: {}", savedProduct.getProductId());

        return productMapper.toProductDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID productId) {
        log.info("Метод getProductById. productId: {}", productId);
        Product product = getProductOrElseThrow(productId);

        log.info("Товар успешно найден: {}", product);
        return productMapper.toProductDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getProductsByCategory(ProductCategory category, Pageable pageable) {
        log.info("Метод getProductsByCategory. поиск по категории: {}.\n" +
                        " Параметры пагинации: sort={}, pageSize:{}, pageNumber:{}",
                category, pageable.getSort(), pageable.getPageSize(), pageable.getPageNumber());

        Page<Product> products = productRepository.findByProductCategory(category, pageable);
        log.info("Количество найденных записей: {}", products.getContent().size());

        return products.map(productMapper::toProductDto);
    }

    @Override
    @Loggable
    @Transactional(readOnly = true)
    public Map<UUID, BigDecimal> getProductsPrice(Set<UUID> productIds) {

        List<Product> products = getProductsOrElseThrow(productIds);
        log.info("Найденные товары: {}", products);

        return products.stream()
                .collect(toMap(Product::getProductId,
                        product -> BigDecimal.valueOf(product.getPrice())));
    }

    @Override
    public ProductDto updateProduct(UpdateProductDto productDto) {
        log.info("Метод updateProduct. DTO: {}", productDto);
        Product productForUpdate = getProductOrElseThrow(productDto.getProductId());
        productMapper.updateProduct(productDto, productForUpdate);
        productRepository.save(productForUpdate);
        log.info("Товар обновлен в БД: {}", productForUpdate);

        return productMapper.toProductDto(productForUpdate);
    }

    @Override
    public boolean updateProductQuantityState(SetProductQuantityStateRequest request) {
        UUID productId = request.getProductId();
        QuantityState quantityState = request.getQuantityState();
        log.info("Метод updateProductQuantityState. productID: {}, quantityState: {}", productId, quantityState);

        Product product = getProductOrElseThrow(productId);
        if (product.getQuantityState() == quantityState) {
            log.info("quantityState товара уже имеет значение: {}", quantityState);
            return false;
        }
        product.setQuantityState(quantityState);
        productRepository.save(product);
        log.info("Товар успешно обновил quantityState. Product: {}", product);

        return true;
    }

    @Override
    public boolean deactivateProduct(UUID productId) {
        log.info("метод deactivateProduct. productID: {}", productId);

        Product product = getProductOrElseThrow(productId);
        if (product.getProductState() == ProductState.DEACTIVATE) {
            log.info("Товар уже деактивирован. Product: {}", product);
            return false;
        }
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        log.info("Товар успешно деактивирован. Product: {}", product);

        return true;
    }

    private Product getProductOrElseThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                            log.info("Товар с Id: {} не найден", productId);
                            return new ProductNotFoundException("Товар с Id: " + productId + " не найден");
                        }
                );
    }

    private List<Product> getProductsOrElseThrow(Set<UUID> productIds) {
        List<Product> products = productRepository.findByProductIdIn(productIds);

        if (products.size() != productIds.size()) {
            Set<UUID> foundIds = products.stream()
                    .map(Product::getProductId)
                    .collect(toSet());

            Set<UUID> missingIds = new HashSet<>(productIds);
            missingIds.removeAll(foundIds);

            throw new ProductNotFoundException("Не найдены товары с id: " + missingIds);
        }

        return products;
    }
}