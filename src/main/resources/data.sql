insert into categories (id, name)
values
    (1, 'Planting materials'),
    (2, 'Protective products and septic tanks'),
    (3, 'Fertilizers'),
    (4, 'Pots and planters'),
    (5, 'Tools and Equipment');

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


insert into users (id, name,email, phone,   role)
values  (1,"Hans Schmidt",  "h.schmidt@example.com", "+49 30 1234567", "CLIENT"),
        (2, "Anna Müller",  "a.mueller@example.com", "+49 89 7654321", "CLIENT"),
        (3, "Peter Klein",  "p.klein@example.com", "+49 40 6789012", "CLIENT"),
        (4, "Maria Schneider",  "m.schneider@example.com", "+49 69 1234567", "ADMIN"),
        (5, "Lukas Fischer",  "l.fischer@example.com", "+49 711 9876543", "CLIENT"),
        (6, "Sophie Weber",  "s.weber@example.com", "+49 221 4567890", "CLIENT"),
        (7, "Max Meyer",  "m.meyer@example.com", "+49 211 2345678", "CLIENT"),
        (8, "Julia Wagner",  "j.wagner@example.com", "+49 351 8765432", "CLIENT"),
        (9, "Anna Becker",  "p.becker@example.com", "+49 341 7654321", "CLIENT"),
        (10, "Clara Hoffmann",  "c.hoffmann@example.com", "+49 421 1234567", "ADMIN");

insert into cart(id, user_id)
values (1, 1),
       (2, 8),
       (3, 5),
       (4, 7);

insert into cart_items (id, cart_id, product_id, quantity)
values (1, 1, 8, 2),
        (2, 1, 6, 1),
        (3, 1, 5, 3),
        (4, 1, 9, 1);

insert into orders(id, user_id, delivery_adress, contact_phone, status, delivery_method)
values (1, 1, "Domstraße 5, 60528 Frankfurt am Main", "+49 30 1234567", "PENDING_PAYMENT", "COURIER_DELIVERY"),
       (2, 8, "Musterstraße 12, 10115 Berlin, Germany", "++49 351 8765432", "IN_TRANSIT", "SELF_DELIVERY"),
       (3, 5, "Bahnhofstraße 8, 80335 München, Germany", "+49 711 9876543", "DELIVERED", "COURIER_DELIVERY"),
       (4, 1, "Domstraße 5, 60528 Frankfurt am Main", "+49 30 1234567", "CANCELED", "SELF_DELIVERY");

INSERT INTO order_items (id,order_id, product_id, quantity, price_at_purchase)
VALUES
    (1, 1, 8, 2, 35.00),
    (2, 1, 6, 1, 15.00),
    (3, 1, 5, 3, 8.00),
    (4, 1, 9, 1, 10.50),
    (5, 2, 3, 5, 2.50),
    (6, 2, 7, 1, 300.00),
    (7, 3, 2, 1, 150.00),
    (8, 3, 4, 2, 5.00),
    (9, 4, 10, 4, 10.00);


INSERT INTO favorites (id, user_id, product_id)
VALUES
    (1, 1, 2),
    (2, 1, 5),
    (3, 2, 7),
    (4, 3, 8),
    (5, 4, 3),
    (6, 5, 6),
    (7, 6, 9),
    (8, 7, 4),
    (9, 8, 1),
    (10, 9, 10);
