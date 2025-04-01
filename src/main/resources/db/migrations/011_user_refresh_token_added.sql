-- liquibase formatted sql
-- changeset DeyanSpasov:011


ALTER TABLE users
    ADD COLUMN refresh_token varchar(255);

ALTER TABLE users
    ADD UNIQUE (email);