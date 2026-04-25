CREATE TABLE IF NOT EXISTS payment
(
    payment_id     UUID PRIMARY KEY,
    order_id       UUID           NOT NULL UNIQUE,
    payment_state  VARCHAR(10)    NOT NULL CHECK ( payment_state IN ('PENDING', 'SUCCESS', 'FAILED')),
    total_payment  NUMERIC(12, 2) NOT NULL CHECK (total_payment > 0),
    product_total  NUMERIC(12, 2) NOT NULL CHECK (product_total > 0),
    delivery_total NUMERIC(12, 2) NOT NULL CHECK (delivery_total > 0),
    fee_total      NUMERIC(12, 2) NOT NULL CHECK (fee_total > 0)
);