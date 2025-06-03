ALTER TABLE users ADD COLUMN enabled BOOLEAN;
ALTER TABLE users ADD COLUMN account_non_expired BOOLEAN;
ALTER TABLE users ADD COLUMN account_non_locked BOOLEAN;
ALTER TABLE users ADD COLUMN credentials_non_expired BOOLEAN;