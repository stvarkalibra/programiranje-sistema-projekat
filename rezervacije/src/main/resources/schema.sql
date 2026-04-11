DROP TABLE IF EXISTS booking_status_log;
DROP TABLE IF EXISTS add_on_item;
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS guest;
DROP TABLE IF EXISTS cancellation_policy;

CREATE TABLE cancellation_policy (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     policy_name VARCHAR(255) NOT NULL,
                                     refund_percentage INT NOT NULL
);

CREATE TABLE guest (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       phone_number VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE booking (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         guest_id BIGINT NOT NULL,
                         policy_id BIGINT NOT NULL,
                         unit_id BIGINT NOT NULL,
                         start_date DATE NOT NULL,
                         end_date DATE NOT NULL,
                         total_price DOUBLE NOT NULL,
                         status VARCHAR(50) NOT NULL,

                         CONSTRAINT fk_booking_guest
                             FOREIGN KEY (guest_id) REFERENCES guest(id),

                         CONSTRAINT fk_booking_policy
                             FOREIGN KEY (policy_id) REFERENCES cancellation_policy(id)
);

CREATE TABLE add_on_item (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             booking_id BIGINT NOT NULL,
                             service_name VARCHAR(255) NOT NULL,
                             price DOUBLE NOT NULL,

                             CONSTRAINT fk_add_on_item_booking
                                 FOREIGN KEY (booking_id) REFERENCES booking(id)
);

CREATE TABLE booking_status_log (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    booking_id BIGINT NOT NULL,
                                    old_status VARCHAR(50),
                                    new_status VARCHAR(50) NOT NULL,
                                    changed_at TIMESTAMP NOT NULL,

                                    CONSTRAINT fk_booking_status_log_booking
                                        FOREIGN KEY (booking_id) REFERENCES booking(id)
);