-- liquibase formatted sql
-- changeset DeyanSpasov:003

SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE orders
ADD COLUMN total_price float(53) NULL;

SET FOREIGN_KEY_CHECKS = 1;