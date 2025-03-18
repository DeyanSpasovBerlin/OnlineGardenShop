-- liquibase formatted sql
-- changeset dmitrii.shkolnyi:010

-- Обновление значений discount_price в таблице products
UPDATE products SET discount_price = NULL WHERE id IN (1, 3, 5, 6, 8, 9, 10);
UPDATE products SET discount_price = 135.00 WHERE id = 2;
UPDATE products SET discount_price = 4.5 WHERE id = 4;
UPDATE products SET discount_price = 250.00 WHERE id = 7;
