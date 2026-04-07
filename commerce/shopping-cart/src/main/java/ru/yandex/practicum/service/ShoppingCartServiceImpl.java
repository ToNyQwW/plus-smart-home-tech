package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dal.entity.ShoppingCart;
import ru.yandex.practicum.dal.repository.ShoppingCartRepository;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.exception.cart.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static ru.yandex.practicum.model.ShoppingCartState.CLOSED;
import static ru.yandex.practicum.model.ShoppingCartState.OPENED;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;
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
        shoppingCart.addProducts(products);
        log.info("товары успешно добавлены в корзину. shoppingCart: {}", shoppingCart);

        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    private void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
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