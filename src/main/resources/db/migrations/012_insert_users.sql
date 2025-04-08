-- liquibase formatted sql
-- changeset DeyanSpasov:012

INSERT INTO users (id, last_name, first_name, email, phone, password, role, refresh_token)
VALUES
    (11, 'Miller', 'Alice', 'alice11@example.com', '+49 170 1234567', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (12, 'Taylor', 'Bob', 'bob12@example.com', '+49 171 2345678', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (13, 'Harris', 'Charlie', 'charlie13@example.com', '+49 172 3456789', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (14, 'Clark', 'David', 'david14@example.com', '+49 173 4567890', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (15, 'Lewis', 'Emma', 'emma15@example.com', '+49 174 5678901', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (16, 'Walker', 'Frank', 'frank16@example.com', '+49 175 6789012', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (17, 'Young', 'Grace', 'grace17@example.com', '+49 176 7890123', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (18, 'King', 'Hannah', 'hannah18@example.com', '+49 177 8901234', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (19, 'Scott', 'Ian', 'ian19@example.com', '+49 178 9012345', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (20, 'Adams', 'Jack', 'jack20@example.com', '+49 179 0123456', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (21, 'Nelson', 'Olivia', 'olivia21@example.com', '+49 170 9876543', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (22, 'Carter', 'Liam', 'liam22@example.com', '+49 171 8765432', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (23, 'Mitchell', 'Sophia', 'sophia23@example.com', '+49 172 7654321', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (24, 'Perez', 'Mason', 'mason24@example.com', '+49 173 6543210', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (25, 'Roberts', 'Ethan', 'ethan25@example.com', '+49 174 5432109', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (26, 'Evans', 'Amelia', 'amelia26@example.com', '+49 175 4321098', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (27, 'Green', 'Lucas', 'lucas27@example.com', '+49 176 3210987', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (28, 'Baker', 'Zoe', 'zoe28@example.com', '+49 177 2109876', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (29, 'Gonzalez', 'Oliver', 'oliver29@example.com', '+49 178 1098765', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL),
    (30, 'Ward', 'Lily', 'lily30@example.com', '+49 179 0987654', '$2a$10$Jj6Kqsvaxjgk7rUuhj1u6uCVnO5RULpC9KbQK381DeaUfb8nQJQji', 'CLIENT', NULL);