CREATE TABLE IF NOT EXISTS orders
(
    order_id         UUID                  DEFAULT gen_random_uuid() PRIMARY KEY,
    shopping_cart_id UUID         NOT NULL,
    payment_id       UUID UNIQUE,
    delivery_id      UUID UNIQUE,
    username         VARCHAR(255) NOT NULL,
    state            VARCHAR(20)  NOT NULL,
    fragile          BOOLEAN      NOT NULL DEFAULT FALSE,
    delivery_weight  FLOAT        NOT NULL CHECK (delivery_weight > 0),
    delivery_volume  FLOAT        NOT NULL CHECK (delivery_volume > 0),
    delivery_price   NUMERIC(12, 2) CHECK (delivery_price > 0 OR delivery_price IS NULL),
    product_price    NUMERIC(12, 2) CHECK (product_price > 0 OR product_price IS NULL),
    total_price      NUMERIC(12, 2) CHECK (total_price > 0 OR total_price IS NULL)
);

CREATE TABLE IF NOT EXISTS order_products
(
    order_id   UUID   NOT NULL REFERENCES orders (order_id) ON DELETE CASCADE,
    product_id UUID   NOT NULL,
    quantity   BIGINT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, product_id)
);
