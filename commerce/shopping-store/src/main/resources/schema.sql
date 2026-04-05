CREATE TYPE QUANTITY_STATE AS ENUM ('ENDED','FEW','ENOUGH','MANY');

CREATE TYPE PRODUCT_STATE AS ENUM ('ACTIVE','DEACTIVATE');

CREATE TYPE PRODUCT_CATEGORY AS ENUM ('LIGHTING','CONTROL','SENSORS');

CREATE TABLE IF NOT EXISTS products
(
    product_id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_name     VARCHAR(255)     NOT NULL,
    description      TEXT             NOT NULL,
    image_src        VARCHAR(500),
    quantity_state   QUANTITY_STATE   NOT NULL,
    product_state    PRODUCT_STATE    NOT NULL,
    product_category PRODUCT_CATEGORY NOT NULL,
    price            DECIMAL(10, 2)   NOT NULL CHECK ( price > 0)
);