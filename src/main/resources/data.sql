CREATE TABLE categories (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL
);

CREATE TABLE products (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          price DECIMAL(10, 2),
                          image_url VARCHAR(255),
                          discount_price DECIMAL(10, 2),
                          created_at DATETIME,
                          updated_at DATETIME,
                          category_id INT,
                          FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       phone VARCHAR(50),
                       role VARCHAR(50) NOT NULL
);

CREATE TABLE cart (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      user_id INT,
                      FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE cartItems (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           cart_id INT,
                           product_id INT,
                           quantity INT,
                           FOREIGN KEY (cart_id) REFERENCES cart(id),
                           FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT,
                        delivery_address VARCHAR(255),
                        contact_phone VARCHAR(50),
                        order_status VARCHAR(50),
                        delivery_method VARCHAR(50),
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

insert into products (id, name, description, price, image_url, discount_price, created_at, updated_at, category_id)
values
    (1, 'Shovel', 'Durable shovel for garden and yard work.', 30.00, 'images/shovel.jpg', NULL, NOW(), NULL, 5),
    (2, 'Electric Lawn Mower', 'Compact lawn mower for easy lawn care.', 150.00, 'images/lawnmower.jpg', 7990.00, NOW(), NOW(), 5),
    (3, 'Tomato Seeds', 'High-yield tomato seeds for planting.', 2.50, 'images/tomato_seeds.jpg', NULL, NOW(), NULL, 1),
    (4, 'Ceramic Pot', 'Ceramic pot for indoor plants.', 5.00, 'images/ceramic_pot.jpg', 850.00, NOW(), NOW(), 4),
    (5, 'Compost Fertilizer', 'Organic fertilizer to improve soil quality.', 8.00, 'images/compost_fertilizer.jpg', NULL, NOW(), NULL, 3),
    (6, 'Insecticide', 'Effective solution for protecting plants from pests.', 15.00, 'images/insecticide.jpg', 600.00, NOW(), NOW(), 2),
    (7, 'Septic Tank', 'Autonomous wastewater treatment system.', 300.00, 'images/septic.jpg', NULL, NOW(), NULL, 2),
    (8, 'Garden Pruner', 'Sharp and ergonomic pruner for trimming plants.', 35.00, 'images/pruner.jpg', NULL, NOW(), NULL, 5),
    (9, 'Hanging Planter', 'Elegant hanging planter for flowers.', 10.50, 'images/hanging_pot.jpg', 1100.00, NOW(), NOW(), 4),
    (10, 'Mineral Fertilizer', 'Comprehensive fertilizer for plant growth.', 10.00, 'images/mineral_fertilizer.jpg', NULL, NOW(), NULL, 3);

insert into categories (id, name)
values
    (1, 'Planting materials'),
    (2, 'Protective products and septic tanks'),
    (3, 'Fertilizers'),
    (4, 'Pots and planters'),
    (5, 'Tools and Equipment');


-- Insert users
DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255),
                       email VARCHAR(255),
                       phone VARCHAR(50),
                       role VARCHAR(50)
);
ALTER TABLE orders DROP FOREIGN KEY fk_orders_users1;

-- Then, drop the orders table (if necessary)
DROP TABLE IF EXISTS orders;

-- Now, drop the users table
DROP TABLE IF EXISTS users;

insert into users ( name, email, phone, role) values
         ( 'Hans Schmidt', 'h.schmidt@example.com', '+49 30 1234567', 'CLIENT'),
         ( 'Anna Müller', 'a.mueller@example.com', '+49 89 7654321', 'CLIENT'),
         ( 'Max Weber', 'm.weber@example.com', '+49 69 2345678', 'ADMIN'),
         ( 'Sophia Keller', 's.keller@example.com', '+49 40 3456789', 'CLIENT'),
         ( 'Lukas Meier', 'l.meier@example.com', '+49 351 8765432', 'CLIENT'),
         ( 'Emma Schneider', 'e.schneider@example.com', '+49 711 9876543', 'CLIENT');

-- Insert carts
insert into cart (id, user_id) values
           (1, 1),
           (2, 8),
           (3, 5),
           (4, 1);

-- Insert cart items
insert into cartItems (id, cart_id, product_id, quantity) values
          (1, 1, 8, 2),
          (2, 1, 6, 1),
          (3, 1, 5, 3),
          (4, 1, 9, 1);

-- Insert orders
insert into orders (id, user_id, delivery_address, contact_phone, order_status, delivery_method) values
         (1, 1, 'Domstraße 5, 60528 Frankfurt am Main', '+49 30 1234567', 'PENDING_PAYMENT', 'COURIER_DELIVERY'),
         (2, 8, 'Musterstraße 12, 10115 Berlin, Germany', '+49 351 8765432', 'IN_TRANSIT', 'SELF_DELIVERY'),
         (3, 5, 'Bahnhofstraße 8, 80335 München, Germany', '+49 711 9876543', 'DELIVERED', 'COURIER_DELIVERY'),
         (4, 1, 'Domstraße 5, 60528 Frankfurt am Main', '+49 30 1234567', 'CANCELED', 'SELF_DELIVERY');


