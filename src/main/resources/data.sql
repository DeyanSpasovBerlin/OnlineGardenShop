

INSERT INTO users (id, last_name, first_name, email, phone, password, role) VALUES
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


-- insert into products (id, name, description, price, image_url, discount_price, created_at, updated_at, category_id)
-- values
--     (1, 'Shovel', 'Durable shovel for garden and yard work.', 30.00, 'images/shovel.jpg', NULL, NOW(), NULL, 5),
--     (2, 'Electric Lawn Mower', 'Compact lawn mower for easy lawn care.', 150.00, 'images/lawnmower.jpg', 7990.00, NOW(), NOW(), 5),
--     (3, 'Tomato Seeds', 'High-yield tomato seeds for planting.', 2.50, 'images/tomato_seeds.jpg', NULL, NOW(), NULL, 1),
--     (4, 'Ceramic Pot', 'Ceramic pot for indoor plants.', 5.00, 'images/ceramic_pot.jpg', 850.00, NOW(), NOW(), 4),
--     (5, 'Compost Fertilizer', 'Organic fertilizer to improve soil quality.', 8.00, 'images/compost_fertilizer.jpg', NULL, NOW(), NULL, 3),
--     (6, 'Insecticide', 'Effective solution for protecting plants from pests.', 15.00, 'images/insecticide.jpg', 600.00, NOW(), NOW(), 2),
--     (7, 'Septic Tank', 'Autonomous wastewater treatment system.', 300.00, 'images/septic.jpg', NULL, NOW(), NULL, 2),
--     (8, 'Garden Pruner', 'Sharp and ergonomic pruner for trimming plants.', 35.00, 'images/pruner.jpg', NULL, NOW(), NULL, 5),
--     (9, 'Hanging Planter', 'Elegant hanging planter for flowers.', 10.50, 'images/hanging_pot.jpg', 1100.00, NOW(), NOW(), 4),
--     (10, 'Mineral Fertilizer', 'Comprehensive fertilizer for plant growth.', 10.00, 'images/mineral_fertilizer.jpg', NULL, NOW(), NULL, 3);

-- insert into categories (id, name)
-- values
--     (1, 'Planting materials'),
--     (2, 'Protective products and septic tanks'),
--     (3, 'Fertilizers'),
--     (4, 'Pots and planters'),
--     (5, 'Tools and Equipment')