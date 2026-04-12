INSERT INTO cancellation_policy (policy_name, refund_percentage) VALUES ('Free cancel 48h before', 100);
INSERT INTO cancellation_policy (policy_name, refund_percentage) VALUES ('50% refund', 50);
INSERT INTO cancellation_policy (policy_name, refund_percentage) VALUES ('No refund', 0);

INSERT INTO guest (first_name, last_name, email, phone_number) VALUES ('Marko', 'Markovic', 'marko@gmail.com', '0611111111');
INSERT INTO guest (first_name, last_name, email, phone_number) VALUES ('Jelena', 'Jovanovic', 'jelena@gmail.com', '0622222222');
INSERT INTO guest (first_name, last_name, email, phone_number) VALUES ('Nikola', 'Nikolic', 'nikola@gmail.com', '0633333333');

INSERT INTO booking (guest_id, policy_id, unit_id, start_date, end_date, total_price, status) VALUES (1, 1, 101, DATE '2026-06-10', DATE '2026-06-15', 500.00, 'PENDING');
INSERT INTO booking (guest_id, policy_id, unit_id, start_date, end_date, total_price, status) VALUES (2, 2, 102, DATE '2026-02-01', DATE '2026-02-05', 300.00, 'COMPLETED');
INSERT INTO booking (guest_id, policy_id, unit_id, start_date, end_date, total_price, status) VALUES (3, 1, 103, DATE '2026-08-12', DATE '2026-08-18', 700.00, 'PENDING');

INSERT INTO add_on_item (booking_id, service_name, price) VALUES (1, 'Breakfast', 15.00);
INSERT INTO add_on_item (booking_id, service_name, price) VALUES (1, 'Parking', 10.00);
INSERT INTO add_on_item (booking_id, service_name, price) VALUES (2, 'Airport transfer', 40.00);

INSERT INTO booking_status_log (booking_id, old_status, new_status, changed_at) VALUES (1, 'PENDING', 'COMPLETED', TIMESTAMP '2026-05-01 10:00:00');
INSERT INTO booking_status_log (booking_id, old_status, new_status, changed_at) VALUES (2, 'PENDING', 'PENDING', TIMESTAMP '2026-05-02 12:00:00');
INSERT INTO booking_status_log (booking_id, old_status, new_status, changed_at) VALUES (3, 'PENDING', 'COMPLETED', TIMESTAMP '2026-05-03 15:30:00');