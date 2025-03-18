-- liquibase formatted sql
-- changeset dmitrii.shkolnyi:007

ALTER TABLE products
DROP FOREIGN KEY fk_products_categories1;

ALTER TABLE products
    ADD CONSTRAINT fk_products_categories1
        FOREIGN KEY (categories_id)
            REFERENCES categories(id)
            ON DELETE SET NULL;
