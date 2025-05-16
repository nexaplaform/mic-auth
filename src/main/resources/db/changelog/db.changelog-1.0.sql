CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(60),
    last_name VARCHAR(60),
    phone_number VARCHAR(15),
    email VARCHAR(100),
    password VARCHAR(100),
    status VARCHAR(10)
);

--INSERT INTO users (first_name, last_name, phone_number, email, password, status) VALUES ('Robert', 'Taylor', '123-555-0101', 'robert.t@example.com', 'pass123', 'ACTIVE');
--INSERT INTO users (first_name, last_name, phone_number, email, password, status) VALUES ('Sophia', 'Anderson', '123-555-0102', 'sophia.a@example.com', 'securepwd', 'ACTIVE');
--INSERT INTO users (first_name, last_name, phone_number, email, password, status) VALUES ('William', 'Thomas', '123-555-0103', 'william.t@example.com', 'mysecret!', 'ACTIVE');
--INSERT INTO users (first_name, last_name, phone_number, email, password, status) VALUES ('Olivia', 'Jackson', '123-555-0104', 'olivia.j@example.com', 'password456', 'ACTIVE');
--INSERT INTO users (first_name, last_name, phone_number, email, password, status) VALUES ('James', 'White', '123-555-0105', 'james.w@example.com', 'testpass', 'ACTIVE');
--INSERT INTO users (first_name, last_name, phone_number, email, password, status) VALUES ('Emma', 'Harris', '123-555-0106', 'emma.h@example.com', 'qwerty', 'ACTIVE');
--INSERT INTO users (first_name, last_name, phone_number, email, password, status) VALUES ('Noah', 'Martin', '123-555-0107', 'noah.m@example.com', 'p@ssword', 'ACTIVE');
--INSERT INTO users (first_name, last_name, phone_number, email, password, status) VALUES ('Isabella', 'Young', '123-555-0108', 'isabella.y@example.com', 'secure123', 'ACTIVE');
--INSERT INTO users (first_name, last_name, phone_number, email, password, status) VALUES ('Lucas', 'King', '123-555-0109', 'lucas.k@example.com', 'kingpass', 'ACTIVE');
--INSERT INTO users (first_name, last_name, phone_number, email, password, status) VALUES ('Mia', 'Scott', '123-555-0110', 'mia.s@example.com', 'miaemma', 'ACTIVE');
--

