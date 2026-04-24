CREATE TABLE IF NOT EXISTS address
(
    address_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    country    VARCHAR(50) NOT NULL,
    city       VARCHAR(50) NOT NULL,
    street     VARCHAR(50) NOT NULL,
    house      VARCHAR(15) NOT NULL,
    flat       VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS delivery
(
    delivery_id     UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    from_address_id UUID        NOT NULL REFERENCES address (address_id),
    to_address_id   UUID        NOT NULL REFERENCES address (address_id),
    order_id        UUID        NOT NULL UNIQUE,
    delivery_state  VARCHAR(16) NOT NULL CHECK (delivery_state IN
                                                ('CREATED', 'IN_PROGRESS', 'DELIVERED', 'FAILED', 'CANCELLED'))
);