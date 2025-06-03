CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(60),
    last_name VARCHAR(60),
    phone_number VARCHAR(15),
    email VARCHAR(100),
    password VARCHAR(100),
    status VARCHAR(10)
);

