-- liquibase formatted sql
-- changeset DeyanSpasov:001

CREATE TABLE IF NOT EXISTS users (
  id INT NOT NULL AUTO_INCREMENT,
  last_name VARCHAR(150) NULL,
  first_name VARCHAR(150) NULL,
  email VARCHAR(45) NULL,
  phone VARCHAR(45) NULL,
  password VARCHAR(100) NULL,
  role VARCHAR(150) NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS cart (
  id INT NOT NULL AUTO_INCREMENT,
  users_id INT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_cart_users
    FOREIGN KEY (users_id)
    REFERENCES users (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS categories (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NULL,
  PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS products (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(150) NULL,
  categories_id INT NULL,
  description VARCHAR(250) NULL,
  price float(53) NULL,
  discount_price float(53) NULL,
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  image_url VARCHAR(150) NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_products_categories1
    FOREIGN KEY (categories_id)
    REFERENCES categories (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS favorites (
  id INT NOT NULL AUTO_INCREMENT,
  users_id INT NULL,
  products_id INT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_FAVORITES_users1
    FOREIGN KEY (users_id)
    REFERENCES users (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_favorites_products
    FOREIGN KEY (products_id)
    REFERENCES products (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS cart_items (
  id INT NOT NULL  AUTO_INCREMENT,
  quantity INT NULL,
  cart_id INT NULL,
  products_id INT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_cart_items_cart1
    FOREIGN KEY (cart_id)
    REFERENCES cart (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_cart_items_products1
    FOREIGN KEY (products_id)
    REFERENCES products (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS orders (
  id INT NOT NULL AUTO_INCREMENT,
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  delivery_address VARCHAR(150) NULL,
  contact_phone VARCHAR(60) NULL,
  delivery_method VARCHAR(60) NULL,
  status VARCHAR(150) NULL,
  users_id INT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_orders_users1
    FOREIGN KEY (users_id)
    REFERENCES users (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS order_items (
  id INT NOT NULL AUTO_INCREMENT,
  quantity INT NULL,
  purchase_price DECIMAL(15,2) NULL,
  orders_id INT NULL,
  products_id INT NULL,
  price_at_purchase FLOAT(53),
  PRIMARY KEY (id),
  CONSTRAINT fk_order_items_orders1
    FOREIGN KEY (orders_id)
    REFERENCES orders (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_order_items_products1
    FOREIGN KEY (products_id)
    REFERENCES products (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

