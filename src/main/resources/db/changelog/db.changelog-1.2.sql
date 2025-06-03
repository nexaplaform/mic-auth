CREATE TABLE ROLES (
    id SERIAL PRIMARY KEY,
    name VARCHAR(60),
    description VARCHAR(255),
    active BOOLEAN,
    created_by VARCHAR(60),
    created_date TIMESTAMP WITH TIME ZONE,
    last_modified_by VARCHAR(60),
    last_modified_date TIMESTAMP WITH TIME ZONE
);