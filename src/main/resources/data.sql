DROP TABLE IF EXISTS `onlineGardenShop`.`orderItems` ;

CREATE TABLE IF NOT EXISTS `onlineGardenShop`.`orderItems` (
  `id` INT NOT NULL,
  `quantity` INT NULL,
  `purchase_price` DECIMAL(15,2) NULL,
    `orders_id` INT NULL,
    `products_id` INT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
    INDEX `fk_orderItems_orders1_idx` (`orders_id` ASC) VISIBLE,
    INDEX `fk_orderItems_products1_idx` (`products_id` ASC) VISIBLE,
    CONSTRAINT `fk_orderItems_orders1`
    FOREIGN KEY (`orders_id`)
    REFERENCES `onlineGardenShop`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_orderItems_products1`
    FOREIGN KEY (`products_id`)
    REFERENCES `onlineGardenShop`.`products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

DROP TABLE IF EXISTS `onlineGardenShop`.`orders` ;

CREATE TABLE IF NOT EXISTS `onlineGardenShop`.`orders` (
   `id` INT NOT NULL,
--    `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
--    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `delivery_address` VARCHAR(150) NULL,
    `contact_phone` VARCHAR(60) NULL,
    `delivery_method` VARCHAR(60) NULL,
    `order_status` ENUM('CREATED', 'PENDING', 'PAID', 'SHIPPED', 'DELIVERED', 'CANCELLED') NULL,
    `users_id` INT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
    INDEX `fk_orders_users1_idx` (`users_id` ASC) VISIBLE,
    CONSTRAINT `fk_orders_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `onlineGardenShop`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

DROP TABLE IF EXISTS `onlineGardenShop`.`users` ;
CREATE TABLE IF NOT EXISTS 'onlineGardenShop'.'users' (
    'id' INT NOT NULL AUTO_INCREMENT,
     'name' VARCHAR(150) NULL,
    'email' VARCHAR(45) NULL,
    'phone' VARCHAR(45) NULL,
    'password' VARCHAR(45) NOT NULL,
    'role' VARCHAR(45) NULL
    PRIMARY KEY ('id'),
    UNIQUE INDEX 'id_UNIQUE' ('id' ASC) VISIBLE)
    ENGINE = InnoDB;



insert into orders(id, user_id, delivery_adress, contact_phone, order_status, delivery_method)
values (1, 1, "Domstraße 5, 60528 Frankfurt am Main", "+49 30 1234567", "PENDING_PAYMENT", "COURIER_DELIVERY"),
       (2, 8, "Musterstraße 12, 10115 Berlin, Germany", "+49 351 8765432", "IN_TRANSIT", "SELF_DELIVERY"),
       (3, 5, "Bahnhofstraße 8, 80335 München, Germany", "+49 711 9876543", "DELIVERED", "COURIER_DELIVERY"),
       (4, 1, "Domstraße 5, 60528 Frankfurt am Main", "+49 30 1234567", "CANCELED", "SELF_DELIVERY")

INSERT INTO users (id, name, email, phone, password, role) VALUES
       (1, 'Alice Johnson', 'alice@example.com', '123-456-7890', 'password123', 'USER'),
       (2, 'Bob Smith', 'bob@example.com', '987-654-3210', 'securePass456', 'ADMIN'),
       (3, 'Charlie Brown', 'charlie@example.com', '555-666-7777', 'charliePass789', 'USER');

