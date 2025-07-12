-- Insert sample end users
INSERT INTO end_users (first_name, last_name, email, mobile_number, password, active, created_at, updated_at)
VALUES
    ('Happy', 'User', 'hpy@user.com', '+1234567890', '{bcrypt}$2a$10$8w22Ivp3S1g.RBdArAP09.hdrC3BVnC0Adez93gLQiFAnSTsV8Y6G', TRUE, NOW(), NOW()),
    ('Happy', 'Admin', 'hpy@admin.com', '+1234567890', '{bcrypt}$2a$10$8w22Ivp3S1g.RBdArAP09.hdrC3BVnC0Adez93gLQiFAnSTsV8Y6G', TRUE, NOW(), NOW()),
    ('Sad', 'User', 'sad@user.com', '+1234567890', '{bcrypt}$2a$10$8w22Ivp3S1g.RBdArAP09.hdrC3BVnC0Adez93gLQiFAnSTsV8Y6G', TRUE, NOW(), NOW()),
    ('Sad', 'Admin', 'sad@admin.com', '+1987654321', '{bcrypt}$2a$10$8w22Ivp3S1g.RBdArAP09.hdrC3BVnC0Adez93gLQiFAnSTsV8Y6G', TRUE, NOW(), NOW());

-- Insert sample authorities
INSERT INTO authorities (name, end_user_id)
VALUES
    ('USER', 1),
    ('ADMIN', 2),
    ('USER', 3),
    ('ADMIN', 4);

