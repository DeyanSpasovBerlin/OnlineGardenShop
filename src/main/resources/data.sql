CREATE TABLE IF NOT EXISTS 'onlineGardenShop'.'users' (
    `id` INT NOT NULL AUTO_INCREMENT,
     'name' VARCHAR(150) NULL,


INSERT INTO users (id, name, email, phone, password, role) VALUES
       (1, 'Alice Johnson', 'alice@example.com', '123-456-7890', 'password123', 'USER'),
       (2, 'Bob Smith', 'bob@example.com', '987-654-3210', 'securePass456', 'ADMIN'),
       (3, 'Charlie Brown', 'charlie@example.com', '555-666-7777', 'charliePass789', 'USER');