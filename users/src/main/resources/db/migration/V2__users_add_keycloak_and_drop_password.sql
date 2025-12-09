-- V2: add keycloak identity columns and drop legacy password
ALTER TABLE public.users
    ADD COLUMN keycloak_id VARCHAR(64) NOT NULL;

ALTER TABLE public.users
    ADD CONSTRAINT uk_users_keycloak_id UNIQUE (keycloak_id);

ALTER TABLE public.users
    ADD COLUMN enabled BOOLEAN;

ALTER TABLE public.users
    ADD COLUMN email_verified BOOLEAN;

ALTER TABLE public.users
    DROP COLUMN password;