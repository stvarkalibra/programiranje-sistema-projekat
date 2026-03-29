INSERT INTO cancellation_policy (id, policy_name, refund_percentage) VALUES (1, 'Free cancel 48h before', 100);
INSERT INTO cancellation_policy (id, policy_name, refund_percentage) VALUES (2, '50% refund', 50);
INSERT INTO cancellation_policy (id, policy_name, refund_percentage) VALUES (3, 'No refund', 0);

INSERT INTO guest (id, first_name, last_name, email, phone_number) VALUES (1, 'Marko', 'Markovic', 'marko@gmail.com', '0611111111');
INSERT INTO guest (id, first_name, last_name, email, phone_number) VALUES (2, 'Jelena', 'Jovanovic', 'jelena@gmail.com', '0622222222');
INSERT INTO guest (id, first_name, last_name, email, phone_number) VALUES (3, 'Nikola', 'Nikolic', 'nikola@gmail.com', '0633333333');

INSERT INTO booking (id, guest_id, policy_id, unit_id, start_date, end_date, total_price, status) VALUES (1, 1, 1, 101, DATE '2026-06-10', DATE '2026-06-15', 500.00, 'CONFIRMED');
INSERT INTO booking (id, guest_id, policy_id, unit_id, start_date, end_date, total_price, status) VALUES (2, 2, 2, 102, DATE '2026-07-01', DATE '2026-07-05', 300.00, 'PENDING');
INSERT INTO booking (id, guest_id, policy_id, unit_id, start_date, end_date, total_price, status) VALUES (3, 3, 1, 103, DATE '2026-08-12', DATE '2026-08-18', 700.00, 'CONFIRMED');

INSERT INTO add_on_item (id, booking_id, service_name, price) VALUES (1, 1, 'Breakfast', 15.00);
INSERT INTO add_on_item (id, booking_id, service_name, price) VALUES (2, 1, 'Parking', 10.00);
INSERT INTO add_on_item (id, booking_id, service_name, price) VALUES (3, 2, 'Airport transfer', 40.00);

INSERT INTO booking_status_log (id, booking_id, old_status, new_status, changed_at) VALUES (1, 1, 'PENDING', 'CONFIRMED', TIMESTAMP '2026-05-01 10:00:00');
INSERT INTO booking_status_log (id, booking_id, old_status, new_status, changed_at) VALUES (2, 2, 'PENDING', 'PENDING', TIMESTAMP '2026-05-02 12:00:00');
INSERT INTO booking_status_log (id, booking_id, old_status, new_status, changed_at) VALUES (3, 3, 'PENDING', 'CONFIRMED', TIMESTAMP '2026-05-03 15:30:00');