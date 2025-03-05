-- insert into users (id, first_name, last_name, email, phone, role)
-- values  (1,"Hans",  "Schmidt", "h.schmidt@example.com", "+49 30 1234567", "CLIENT"),
--         (2, "Anna",  "Müller","a.mueller@example.com", "+49 89 7654321", "CLIENT"),
--         (3, "Peter",  "Klein", "p.klein@example.com", "+49 40 6789012", "CLIENT"),
--         (4, "Maria",  "Schneider","m.schneider@example.com", "+49 69 1234567", "ADMIN"),
--         (5, "Lukas",  "Fischer", "l.fischer@example.com", "+49 711 9876543", "CLIENT"),
--         (6, "Sophie",  "Weber", "s.weber@example.com", "+49 221 4567890", "CLIENT"),
--         (7, "Max",  "Meyer", "m.meyer@example.com", "+49 211 2345678", "CLIENT"),
--         (8, "Julia",  "Wagner", "j.wagner@example.com", "+49 351 8765432", "CLIENT"),
--         (9, "Anna",  "Becker", "p.becker@example.com", "+49 341 7654321", "CLIENT"),
--         (10, "Clara",  "Hoffmann", "c.hoffmann@example.com", "+49 421 1234567", "ADMIN");

INSERT INTO users (id, last_name, first_name, email, phone, password, role)
VALUES
       (1, 'Johnson', 'Alice', 'alice@example.com', '1234567890', 'password123', 'ADMIN'),
       (2, 'Smith', 'Bob', 'bob@example.com', '2345678901', 'securePass1', 'CLIENT'),
       (3, 'Brown', 'Charlie', 'charlie@example.com', '3456789012', 'charliePass', 'CLIENT'),
       (4, 'White', 'David', 'david@example.com', '4567890123', 'davidSecure', 'ADMIN'),
       (5, 'Black', 'Emma', 'emma@example.com', '5678901234', 'emma123', 'CLIENT'),
       (6, 'Green', 'Frank', 'frank@example.com', '6789012345', 'frankPass', 'CLIENT'),
       (7, 'Miller', 'Grace', 'grace@example.com', '7890123456', 'graceSecure', 'CLIENT'),
       (8, 'Wilson', 'Hannah', 'hannah@example.com', '8901234567', 'hannahPass', 'CLIENT'),
       (9, 'Thomas', 'Ian', 'ian@example.com', '9012345678', 'ianSecure', 'CLIENT'),
       (10, 'Davis', 'Jack', 'jack@example.com', '0123456789', 'jackPass', 'CLIENT');

insert into cart(id, user_id)
values (1, 1),
       (2, 8),
       (3, 5),
       -- У нас для cart и user установлено отношение "1 к 1", т.е. у одного пользователя не может быть более 1 корзины.
       (4, 2);

insert into categories (id, name)
values
    (1, 'Planting materials'),
    (2, 'Protective products and septic tanks'),
    (3, 'Fertilizers'),
    (4, 'Pots and planters'),
    (5, 'Tools and Equipment');

insert into products (id, name, category_id, description, price, image_url, discount_price, created_at, updated_at)
values
    (1, 'Shovel', 5, 'Durable shovel for garden and yard work.', 30.00, 'images/shovel.jpg', 0.00, NOW(), NULL),
    (2, 'Electric Lawn Mower', 5, 'Compact lawn mower for easy lawn care.', 150.00, 'images/lawnmower.jpg', 7990.00, NOW(), NULL),
    (3, 'Tomato Seeds', 1, 'High-yield tomato seeds for planting.', 2.50, 'images/tomato_seeds.jpg', 0.00, NOW(), NULL),
    (4, 'Ceramic Pot', 4, 'Ceramic pot for indoor plants.', 5.00, 'images/ceramic_pot.jpg', 850.00, NOW(), NULL),
    (5, 'Compost Fertilizer', 3, 'Organic fertilizer to improve soil quality.', 8.00, 'images/compost_fertilizer.jpg', 0.00, NOW(), NULL),
    (6, 'Insecticide', 2, 'Effective solution for protecting plants from pests.', 15.00, 'images/insecticide.jpg', 600.00, NOW(), NULL),
    (7, 'Septic Tank', 2, 'Autonomous wastewater treatment system.', 300.00, 'images/septic.jpg', 0.00, NOW(), NULL),
    (8, 'Garden Pruner', 5, 'Sharp and ergonomic pruner for trimming plants.', 35.00, 'images/pruner.jpg', 0.00, NOW(), NULL),
    (9, 'Hanging Planter', 4, 'Elegant hanging planter for flowers.', 10.50, 'images/hanging_pot.jpg', 1100.00, NOW(), NULL),
    (10, 'Mineral Fertilizer', 3, 'Comprehensive fertilizer for plant growth.', 10.00, 'images/mineral_fertilizer.jpg', 0.00, NOW(), NULL);

insert into cart_items (id, cart_id, product_id, quantity)
values (1, 1, 8, 2),
        (2, 1, 6, 1),
        (3, 1, 5, 3),
        (4, 1, 9, 1);

insert into orders(id, user_id, delivery_address, contact_phone, status, delivery_method)
values (1, 1, "Domstraße 5, 60528 Frankfurt am Main", "+49 30 1234567", "PENDING_PAYMENT", "COURIER_DELIVERY"),
       (2, 8, "Musterstraße 12, 10115 Berlin, Germany", "++49 351 8765432", "DELIVERED", "SELF_DELIVERY"),
       (3, 5, "Bahnhofstraße 8, 80335 München, Germany", "+49 711 9876543", "SHIPPED", "COURIER_DELIVERY"),
       (4, 1, "Domstraße 5, 60528 Frankfurt am Main", "+49 30 1234567", "CANCELED", "SELF_DELIVERY");
