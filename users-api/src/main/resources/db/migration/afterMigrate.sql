INSERT INTO users(username, email, password)
VALUES ('batman', 'batman@ga.com', 'bat'),
       ('superman', 'superman@ga.com', 'super'),
       ('admin', 'admin@email', '$2a$10$fiUD/QL/tYSl5OSbeqvheeWYq5lgrLktWpdfxoe7tCHuZPxubv.dq');

INSERT INTO roles(name) VALUES('ROLE_USER'),('ROLE_ADMIN');

INSERT INTO user_role(user_id, role_id) VALUES(3,2);