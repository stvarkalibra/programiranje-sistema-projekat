INSERT INTO tax_rate (tax_name, percentage) VALUES ('Standard VAT', 20.0);
INSERT INTO tax_rate (tax_name, percentage) VALUES ('Reduced VAT', 10.0);
INSERT INTO tax_rate (tax_name, percentage) VALUES ('No VAT', 0.0);

INSERT INTO payment_method (method_name, is_active) VALUES ('Cash', true);
INSERT INTO payment_method (method_name, is_active) VALUES ('Credit Card', true);
INSERT INTO payment_method (method_name, is_active) VALUES ('Bank Transfer', true);

INSERT INTO invoice (booking_id, tax_rate_tax_rate_id, subtotal, total_amount, status) VALUES (1, 1, 500.00, 600.00, 'PAID');
INSERT INTO invoice (booking_id, tax_rate_tax_rate_id, subtotal, total_amount, status) VALUES (2, 2, 300.00, 330.00, 'SENT');
INSERT INTO invoice (booking_id, tax_rate_tax_rate_id, subtotal, total_amount, status) VALUES (3, 1, 700.00, 840.00, 'PAID');

INSERT INTO transaction (invoice_id, payment_method_id, amount, transaction_date) VALUES (1, 1, 600.00, DATE '2026-05-01');
INSERT INTO transaction (invoice_id, payment_method_id, amount, transaction_date) VALUES (2, 2, 150.00, DATE '2026-05-02');
INSERT INTO transaction (invoice_id, payment_method_id, amount, transaction_date) VALUES (3, 3, 840.00, DATE '2026-05-03');