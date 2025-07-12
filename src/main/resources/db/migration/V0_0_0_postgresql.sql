CREATE TABLE end_users (
                           id BIGSERIAL PRIMARY KEY,
                           first_name VARCHAR(255),
                           last_name VARCHAR(255),
                           email VARCHAR(255),
                           mobile_number VARCHAR(255),
                           password VARCHAR(255),
                           active BOOLEAN,
                           created_at TIMESTAMP,
                           updated_at TIMESTAMP
);

CREATE TABLE authorities (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255),
                             end_user_id BIGINT REFERENCES end_users(id) ON DELETE CASCADE
);