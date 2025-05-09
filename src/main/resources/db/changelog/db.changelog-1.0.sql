CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(60),
    lastName VARCHAR(60),
    phoneNumber VARCHAR(15),
    email VARCHAR(100),
    password VARCHAR(100)
);

INSERT INTO users (firstName, lastName, phoneNumber, email, password) VALUES ('Robert', 'Taylor', '123-555-0101', 'robert.t@example.com', 'pass123');
INSERT INTO users (firstName, lastName, phoneNumber, email, password) VALUES ('Sophia', 'Anderson', '123-555-0102', 'sophia.a@example.com', 'securepwd');
INSERT INTO users (firstName, lastName, phoneNumber, email, password) VALUES ('William', 'Thomas', '123-555-0103', 'william.t@example.com', 'mysecret!');
INSERT INTO users (firstName, lastName, phoneNumber, email, password) VALUES ('Olivia', 'Jackson', '123-555-0104', 'olivia.j@example.com', 'password456');
INSERT INTO users (firstName, lastName, phoneNumber, email, password) VALUES ('James', 'White', '123-555-0105', 'james.w@example.com', 'testpass');
INSERT INTO users (firstName, lastName, phoneNumber, email, password) VALUES ('Emma', 'Harris', '123-555-0106', 'emma.h@example.com', 'qwerty');
INSERT INTO users (firstName, lastName, phoneNumber, email, password) VALUES ('Noah', 'Martin', '123-555-0107', 'noah.m@example.com', 'p@ssword');
INSERT INTO users (firstName, lastName, phoneNumber, email, password) VALUES ('Isabella', 'Young', '123-555-0108', 'isabella.y@example.com', 'secure123');
INSERT INTO users (firstName, lastName, phoneNumber, email, password) VALUES ('Lucas', 'King', '123-555-0109', 'lucas.k@example.com', 'kingpass');
INSERT INTO users (firstName, lastName, phoneNumber, email, password) VALUES ('Mia', 'Scott', '123-555-0110', 'mia.s@example.com', 'miaemma');


