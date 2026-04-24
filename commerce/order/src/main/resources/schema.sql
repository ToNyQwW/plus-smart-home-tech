CREATE TABLE IF NOT EXISTS address
(
    address_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    country    VARCHAR(50) NOT NULL,
    city       VARCHAR(50) NOT NULL,
    street     VARCHAR(50) NOT NULL,
    house      VARCHAR(15) NOT NULL,
    flat       VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS orders
(
    order_id            UUID           DEFAULT gen_random_uuid() PRIMARY KEY,
    delivery_address_id UUID           NOT NULL REFERENCES address (address_id),
    shopping_cart_id    UUID           NOT NULL,
    payment_id          UUID           NOT NULL UNIQUE,
    delivery_id         UUID           NOT NULL UNIQUE,
    username            VARCHAR(255)   NOT NULL,
    state               VARCHAR(20)    NOT NULL,
    fragile             BOOLEAN        NOT NULL DEFAULT FALSE,
    delivery_weight     FLOAT          NOT NULL CHECK (delivery_weight > 0),
    delivery_volume     FLOAT          NOT NULL CHECK (delivery_volume > 0),
    delivery_price      NUMERIC(12, 2) NOT NULL CHECK (delivery_price > 0),
    product_price       NUMERIC(12, 2) NOT NULL CHECK (product_price > 0),
    total_price         NUMERIC(12, 2) NOT NULL CHECK (total_price > 0)
);

CREATE TABLE IF NOT EXISTS order_products
(
    order_id   UUID   NOT NULL REFERENCES orders (order_id) ON DELETE CASCADE,
    product_id UUID   NOT NULL,
    quantity   BIGINT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, product_id)
);
