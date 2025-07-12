CREATE TABLE end_users (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(255),
                           last_name VARCHAR(255),
                           email VARCHAR(255),
                           mobile_number VARCHAR(255),
                           password VARCHAR(255),
                           active BOOLEAN,
                           created_at DATETIME,
                           updated_at DATETIME
);

CREATE TABLE authorities (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255),
                             end_user_id BIGINT,
                             CONSTRAINT fk_end_user
                                 FOREIGN KEY (end_user_id)
                                     REFERENCES end_users(id)
                                     ON DELETE CASCADE
);