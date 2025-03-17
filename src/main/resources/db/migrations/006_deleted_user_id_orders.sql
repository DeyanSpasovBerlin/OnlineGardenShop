-- liquibase formatted sql
-- changeset DeyanSpasov:006

SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE orders ADD COLUMN deleted_user_id INT NULL AFTER users_id;

SET FOREIGN_KEY_CHECKS = 1;