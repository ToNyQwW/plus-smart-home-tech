CREATE TABLE IF NOT EXISTS warehouse_products
(
    product_id UUID             DEFAULT gen_random_uuid() PRIMARY KEY,
    fragile    BOOLEAN NOT NULL DEFAULT FALSE,
    width      FLOAT   NOT NULL CHECK (width >= 1),
    height     FLOAT   NOT NULL CHECK ( height >= 1),
    depth      FLOAT   NOT NULL CHECK (depth >= 1),
    weight     FLOAT   NOT NULL CHECK ( weight >= 1),
    quantity   BIGINT  NOT NULL DEFAULT 0 CHECK ( quantity >= 0 )
);

CREATE TABLE IF NOT EXISTS orders
(
    order_id UUID PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS order_product
(
    order_id   UUID   NOT NULL REFERENCES orders(order_id),
    product_id UUID   NOT NULL REFERENCES warehouse_products (product_id),
    quantity   BIGINT NOT NULL CHECK (quantity >= 0),
    PRIMARY KEY (order_id, product_id)
);