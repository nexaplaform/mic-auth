CREATE TABLE groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    unique_name VARCHAR(50),
    enabled BOOLEAN,
    description VARCHAR(255),
    created_by VARCHAR(60),
    created_date TIMESTAMP WITH TIME ZONE,
    last_modified_by VARCHAR(60),
    last_modified_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE group_roles(
    group_id INT REFERENCES groups(id),
    role_id INT REFERENCES roles(id),
    PRIMARY KEY (group_id, role_id)
);

CREATE TABLE group_users(
   group_id INT REFERENCES groups(id),
    user_id INT REFERENCES users(id),
    PRIMARY KEY (group_id, user_id)
);

ALTER TABLE groups ADD CONSTRAINT uq_group_unique_name UNIQUE (unique_name);