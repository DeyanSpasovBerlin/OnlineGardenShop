-- liquibase formatted sql
-- changeset DeyanSpasov:007

SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE orders ADD COLUMN email_sent BOOLEAN NOT NULL DEFAULT FALSE;

SET FOREIGN_KEY_CHECKS = 1;