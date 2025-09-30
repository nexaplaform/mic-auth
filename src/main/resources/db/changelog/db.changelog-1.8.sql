ALTER TABLE users DROP CONSTRAINT uq_role;

ALTER TABLE users ADD CONSTRAINT users_up_email UNIQUE (email);
ALTER TABLE roles ADD CONSTRAINT roles_uq_name UNIQUE (name);