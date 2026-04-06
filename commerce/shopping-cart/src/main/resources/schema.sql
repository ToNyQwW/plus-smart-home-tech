CREATE TABLE IF NOT EXISTS shopping_carts
(
    cart_id    UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    cart_state VARCHAR(10)  NOT NULL CHECK (cart_state IN ('OPENED', 'CLOSED'))
);

CREATE TABLE IF NOT EXISTS shopping_carts_products
(
    cart_id    UUID   NOT NULL REFERENCES shopping_carts (cart_id) ON DELETE CASCADE,
    product_id UUID   NOT NULL,
    quantity   BIGINT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (cart_id, product_id)
);