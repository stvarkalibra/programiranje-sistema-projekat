DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS invoice;
DROP TABLE IF EXISTS payment_method;
DROP TABLE IF EXISTS tax_rate;


CREATE TABLE tax_rate (
                          tax_rate_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          tax_name VARCHAR(255) NOT NULL,
                          percentage DOUBLE NOT NULL
);


CREATE TABLE payment_method (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                method_name VARCHAR(255) NOT NULL,
                                is_active BOOLEAN NOT NULL
);


CREATE TABLE invoice (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         bookind_id BIGINT NOT NULL,
                         tax_rate_id BIGINT NOT NULL,
                         subtotal DOUBLE NOT NULL,
                         total_amount DOUBLE NOT NULL,
                         status VARCHAR(50) NOT NULL,

                         CONSTRAINT fk_invoice_tax_rate
                             FOREIGN KEY (tax_rate_id) REFERENCES tax_rate(tax_rate_id)
);


CREATE TABLE transaction (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             invoice_id BIGINT NOT NULL,
                             payment_method_id BIGINT NOT NULL,
                             amount DOUBLE NOT NULL,
                             transaction_date DATE NOT NULL,

                             CONSTRAINT fk_transaction_invoice
                                 FOREIGN KEY (invoice_id) REFERENCES invoice(id),

                             CONSTRAINT fk_transaction_payment_method
                                 FOREIGN KEY (payment_method_id) REFERENCES payment_method(id)
);