package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dal.entity.Product;
import ru.yandex.practicum.dal.repository.ProductRepository;
import ru.yandex.practicum.dto.store.CreateProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.store.UpdateProductDto;
import ru.yandex.practicum.exception.store.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.ProductState;
import ru.yandex.practicum.model.QuantityState;

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

    @Override
    public boolean deactivateProduct(UUID productId) {
        log.info("метод deactivateProduct. productID: {}", productId);

        Product product = getProductOrElseThrow(productId);
        if (product.getProductState() == ProductState.DEACTIVATE) {
            log.info("Продукт уже деактивирован. Product: {}", product);
            return false;
        }
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        log.info("Продукт успешно деактивирован. Product: {}", product);

        return true;
    }

    @Override
    public boolean updateProductQuantityState(SetProductQuantityStateRequest request) {
        UUID productId = request.getProductId();
        QuantityState quantityState = request.getQuantityState();
        log.info("метод updateProductQuantityState. productID: {}, quantityState: {}", productId, quantityState);

        Product product = getProductOrElseThrow(productId);
        if (product.getQuantityState() == quantityState) {
            log.info("quantityState продукта уже имеет значение: {}", quantityState);
            return false;
        }
        product.setQuantityState(quantityState);
        productRepository.save(product);
        log.info("Продукт успешно обновил quantityState. Product: {}", product);

        return true;
    }

    private Product getProductOrElseThrow(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }
}