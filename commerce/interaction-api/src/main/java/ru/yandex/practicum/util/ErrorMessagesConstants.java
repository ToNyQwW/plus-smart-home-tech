package ru.yandex.practicum.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessagesConstants {

    public static final String UNAUTHORIZED = "Unauthorized";

    public static final String ORDER_NOT_FOUND = "Order not found";

    public static final String PRODUCT_NOT_FOUND = "Product not found";

    public static final String DELIVERY_NOT_FOUND = "Delivery not found";

    public static final String INVALID_ORDER_STATE = "Invalid order state";

    public static final String INVALID_DELIVERY_STATE = "Invalid delivery state";

    public static final String PRODUCT_ALREADY_EXISTS = "Product already exists";

    public static final String PAYMENT_ALREADY_EXISTS = "Payment already exists";

    public static final String DELIVERY_ALREADY_EXISTS = "Delivery already exists";

    public static final String SHOPPING_CART_NOT_FOUND = "Shopping cart not found";

    public static final String LOW_QUANTITY_IN_WAREHOUSE = "Low Quantity in Warehouse";

    public static final String PAYMENT_SERVICE_UNAVAILABLE = "Payment service unavailable";

    public static final String DELIVERY_SERVICE_UNAVAILABLE = "Delivery service unavailable";

    public static final String WAREHOUSE_SERVICE_UNAVAILABLE = "Warehouse service unavailable";

    public static final String NO_PRODUCTS_IN_SHOPPING_CART = "Products int shopping cart not found";

    public static final String SHOPPING_STORE_SERVICE_UNAVAILABLE = "Shopping-store service unavailable";
}