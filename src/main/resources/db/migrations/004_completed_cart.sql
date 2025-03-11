-- liquibase formatted sql
-- changeset DeyanSpasov:004

SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE cart
ADD COLUMN completed BOOLEAN NOT NULL DEFAULT FALSE;


SET FOREIGN_KEY_CHECKS = 1;