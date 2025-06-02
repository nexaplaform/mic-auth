CREATE TABLE USER_ROLES (
   user_id INT REFERENCES users(id),
   role_id INT REFERENCES roles(id),
   PRIMARY KEY (user_id, role_id)
);