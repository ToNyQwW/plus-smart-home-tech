package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.dal.repository.ProductRepository;
import ru.yandex.practicum.dto.store.CreateProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.UpdateProductDto;
import ru.yandex.practicum.exception.store.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public ProductDto createProduct(CreateProductDto productDto) {
        log.info("метод createProduct. DTO: {}", productDto);
        Product product = productMapper.toProduct(productDto);
        Product savedProduct = productRepository.save(product);
        log.info("Product сохранен в БД с ID: {}", savedProduct.getProductId());

        return productMapper.toProductDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(UpdateProductDto productDto) {
        log.info("метод updateProduct. DTO: {}", productDto);
        Product productForUpdate = getProductOrElseThrow(productDto.getProductId());
        productMapper.updateProduct(productDto, productForUpdate);
        productRepository.save(productForUpdate);
        log.info("Product обновлен в БД: {}", productForUpdate);

        return productMapper.toProductDto(productForUpdate);
    }

    private Product getProductOrElseThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }
}