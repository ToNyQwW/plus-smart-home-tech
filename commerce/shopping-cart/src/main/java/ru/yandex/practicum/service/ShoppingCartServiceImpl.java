package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.WarehouseClientFallback;
import ru.yandex.practicum.dal.entity.ShoppingCart;
import ru.yandex.practicum.dal.repository.ShoppingCartRepository;
import ru.yandex.practicum.dto.commerce.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.commerce.cart.ShoppingCartDto;
import ru.yandex.practicum.exception.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.exception.cart.ShoppingCartNotFoundException;
import ru.yandex.practicum.exception.warehouse.LowQuantityException;
import ru.yandex.practicum.exception.warehouse.WarehouseServiceUnavailableException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;

import java.util.*;

import static ru.yandex.practicum.model.ShoppingCartState.CLOSED;
import static ru.yandex.practicum.model.ShoppingCartState.OPENED;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {


    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClientFallback warehouseClient;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        validateUsername(username);
        log.info("метод getShoppingCart. username: {}", username);
        ShoppingCart shoppingCart = getOrCreateShoppingCart(username);

        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addProducts(String username, Map<UUID, Long> products) {
        validateUsername(username);
        log.info("метод addProducts. username: {}, products: {}", username, products);

        ShoppingCart shoppingCart = getOrCreateShoppingCart(username);

        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toShoppingCartDto(shoppingCart);
        shoppingCartDto.getProducts().putAll(products);

        try {
            log.info("Запрос к сервису Warehouse через Feign client для проверки доступности товаров");
            warehouseClient.checkProductQuantityState(shoppingCartDto);
            shoppingCart.addProducts(products);
            log.info("товары успешно добавлены в корзину. shoppingCart: {}", shoppingCart);

            return shoppingCartDto;

        } catch (WarehouseServiceUnavailableException e) {
            throw e;
        } catch (FeignException e) {
            throw new LowQuantityException("Недостаточно товаров на складе");
        }
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<UUID> products) {
        validateUsername(username);
        log.info("метод removeProducts. username: {}, products: {}", username, products);

        ShoppingCart shoppingCart = getActiveShoppingCartOrElseThrow(username);
        Map<UUID, Long> cartProducts = shoppingCart.getProducts();

        List<UUID> missingProducts = products.stream()
                .filter(id -> !cartProducts.containsKey(id))
                .toList();

        if (!missingProducts.isEmpty()) {
            log.info("в корзине пользователя: {} нет товаров с id: {}", username, missingProducts);
            throw new NoProductsInShoppingCartException(
                    "в корзине пользователя: " + username + " нет товаров с id: " + missingProducts
            );
        }
        products.forEach(cartProducts::remove);
        log.info("товары успешно удалены из корзины. shoppingCart: {}", shoppingCart);

        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        validateUsername(username);
        log.info("метод changeProductQuantity. username: {}, request: {}", username, request);

        ShoppingCart shoppingCart = getActiveShoppingCartOrElseThrow(username);

        UUID productId = request.getProductId();
        Map<UUID, Long> cartProducts = shoppingCart.getProducts();

        if (!cartProducts.containsKey(productId)) {
            log.info("в корзине пользователя: {} нет товара с id: {}", username, productId);
            throw new NoProductsInShoppingCartException(
                    "в корзине пользователя: " + username + " нет товара с id: " + productId
            );
        }
        cartProducts.put(productId, request.getNewQuantity());
        log.info("товар c id {} успешно обновлен. shoppingCart: {}", productId, shoppingCart);

        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public void deactivateShoppingCart(String username) {
        validateUsername(username);
        log.info("метод deactivateShoppingCart. username: {}", username);

        Optional<ShoppingCart> shoppingCartOpt = shoppingCartRepository.findByUserName(username);
        if (shoppingCartOpt.isEmpty()) {
            log.info("у пользователя: {} нет корзины", username);
            throw new ShoppingCartNotFoundException("у пользователя: " + username + " нет корзины");
        }
        ShoppingCart shoppingCart = shoppingCartOpt.get();
        if (shoppingCart.getCartState() == OPENED) {
            shoppingCart.setCartState(CLOSED);
        }
        log.info("корзина пользователя: {} c id: {} деактивирована", username, shoppingCart.getCartId());
    }

    private void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
    }

    private ShoppingCart getActiveShoppingCartOrElseThrow(String username) {
        Optional<ShoppingCart> shoppingCartOpt = shoppingCartRepository.findByUserNameAndCartState(username, OPENED);
        if (shoppingCartOpt.isEmpty()) {
            log.info("у пользователя: {} нет активной корзины", username);
            throw new ShoppingCartNotFoundException("у пользователя: " + username + " нет активной корзины");
        }
        return shoppingCartOpt.get();
    }

    private ShoppingCart getOrCreateShoppingCart(String username) {
        Optional<ShoppingCart> shoppingCartOpt = shoppingCartRepository.findByUserName(username);
        if (shoppingCartOpt.isEmpty()) {
            ShoppingCart shoppingCart = ShoppingCart.builder()
                    .userName(username)
                    .build();
            shoppingCartRepository.save(shoppingCart);
            log.info("создана новая корзина c id: {},  для username: {}", shoppingCart.getCartId(), username);
            return shoppingCart;
        }

        ShoppingCart shoppingCart = shoppingCartOpt.get();
        UUID cartId = shoppingCart.getCartId();
        if (shoppingCart.getCartState() == CLOSED) {
            log.info("корзина с id: {} имеет статус CLOSED", cartId);
            shoppingCart.setProducts(new HashMap<>());
            shoppingCart.setCartState(OPENED);

            log.info("Изменен статус на OPENED и очищена корзина для username: {}", username);
            return shoppingCart;
        }
        log.info("найдена корзина с id: {} для пользователя: {}", cartId, username);
        return shoppingCart;
    }
}