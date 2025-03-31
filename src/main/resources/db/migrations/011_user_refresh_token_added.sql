-- liquibase formatted sql
-- changeset DeyanSpasov:011


ALTER TABLE user
    ADD COLUMN refresh_token varchar(255);

ALTER TABLE user
    ADD UNIQUE (email);