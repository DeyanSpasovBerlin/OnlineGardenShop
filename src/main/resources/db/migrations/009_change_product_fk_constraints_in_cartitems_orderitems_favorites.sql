-- liquibase formatted sql
-- changeset dmitrii.shkolnyi:009

-- Изменяем внешний ключ для cart_items (удаляем товары из корзины при удалении продукта)
ALTER TABLE cart_items
DROP FOREIGN KEY fk_cart_items_products1;

ALTER TABLE cart_items
    ADD CONSTRAINT fk_cart_items_products1
        FOREIGN KEY (products_id)
            REFERENCES products(id)
            ON DELETE CASCADE;

-- Изменяем внешний ключ для order_items (оставляем заказы, но обнуляем product_id)
ALTER TABLE order_items
DROP FOREIGN KEY fk_order_items_products1;

ALTER TABLE order_items
    ADD CONSTRAINT fk_order_items_products1
        FOREIGN KEY (products_id)
            REFERENCES products(id)
            ON DELETE SET NULL;

-- Изменяем внешний ключ для favorites (продукт остается, но помечается как недоступный)
ALTER TABLE favorites
DROP FOREIGN KEY fk_favorites_products;

ALTER TABLE favorites
    ADD CONSTRAINT fk_favorites_products
        FOREIGN KEY (products_id)
            REFERENCES products(id)
            ON DELETE NO ACTION;

