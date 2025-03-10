CREATE TABLE cart
(
    id       INT AUTO_INCREMENT NOT NULL,
    users_id INT                NULL,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

CREATE TABLE cart_items
(
    id          INT AUTO_INCREMENT NOT NULL,
    quantity    INT                NULL,
    cart_id     INT                NOT NULL,
    products_id INT                NOT NULL,
    CONSTRAINT pk_cart_items PRIMARY KEY (id)
);

CREATE TABLE categories
(
    id   INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)       NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE favorites
(
    id          INT AUTO_INCREMENT NOT NULL,
    users_id    INT                NULL,
    products_id INT                NULL,
    CONSTRAINT pk_favorites PRIMARY KEY (id)
);

CREATE TABLE order_items
(
    id                INT AUTO_INCREMENT NOT NULL,
    orders_id         INT                NULL,
    products_id       INT                NULL,
    quantity          INT                NOT NULL,
    price_at_purchase DOUBLE             NOT NULL,
    CONSTRAINT pk_order_items PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id               INT AUTO_INCREMENT NOT NULL,
    users_id         INT                NULL,
    delivery_address VARCHAR(255)       NULL,
    contact_phone    VARCHAR(255)       NULL,
    status           VARCHAR(255)       NULL,
    delivery_method  VARCHAR(255)       NULL,
    created_at       datetime           NOT NULL,
    updated_at       datetime           NULL,
    total_price      DOUBLE             NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE products
(
    id             INT AUTO_INCREMENT NOT NULL,
    name           VARCHAR(255)       NULL,
    `description`  VARCHAR(255)       NULL,
    price          DOUBLE             NOT NULL,
    discount_price DOUBLE             NOT NULL,
    image_url      VARCHAR(255)       NULL,
    created_at     datetime           NOT NULL,
    updated_at     datetime           NULL,
    categories_id  INT                NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         INT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255)       NULL,
    last_name  VARCHAR(255)       NULL,
    email      VARCHAR(255)       NULL,
    phone      VARCHAR(255)       NULL,
    password   VARCHAR(255)       NULL,
    `role`     VARCHAR(255)       NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE cart
    ADD CONSTRAINT uc_cart_users UNIQUE (users_id);

ALTER TABLE cart_items
    ADD CONSTRAINT FK_CART_ITEMS_ON_CART FOREIGN KEY (cart_id) REFERENCES cart (id);

ALTER TABLE cart_items
    ADD CONSTRAINT FK_CART_ITEMS_ON_PRODUCTS FOREIGN KEY (products_id) REFERENCES products (id);

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_USERS FOREIGN KEY (users_id) REFERENCES users (id);

ALTER TABLE favorites
    ADD CONSTRAINT FK_FAVORITES_ON_PRODUCTS FOREIGN KEY (products_id) REFERENCES products (id);

ALTER TABLE favorites
    ADD CONSTRAINT FK_FAVORITES_ON_USERS FOREIGN KEY (users_id) REFERENCES users (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_USERS FOREIGN KEY (users_id) REFERENCES users (id);

ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_ORDERS FOREIGN KEY (orders_id) REFERENCES orders (id);

ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_PRODUCTS FOREIGN KEY (products_id) REFERENCES products (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_CATEGORIES FOREIGN KEY (categories_id) REFERENCES categories (id);