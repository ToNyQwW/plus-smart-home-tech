package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.ProductDto;

@Service
@Transactional
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

//    private final ProductMapper productMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        return null;
    }
}