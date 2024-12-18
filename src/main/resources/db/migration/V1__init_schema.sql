CREATE TABLE payment_methods (
                                 id SERIAL PRIMARY KEY,
                                 name VARCHAR(50) NOT NULL UNIQUE,
                                 validation_rules JSONB NOT NULL,
                                 price_modifier DECIMAL(5, 2) DEFAULT 1.00 NOT NULL,
                                 points_modifier DECIMAL(5, 2) DEFAULT 0.00 NOT NULL,
                                 active BOOLEAN DEFAULT TRUE NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO payment_methods (name, validation_rules, price_modifier, points_modifier)
VALUES
    ('CASH', '{"requiredFields": [], "fieldTypes": {}}', 0.9, 0.05),
    ('CASH_ON_DELIVERY', '{"requiredFields": ["courier"], "fieldTypes": {"courier": "string"},
          "customChecks": {"courier": {"allowedValues": ["YAMATO", "SAGAWA"]}}}', 1.02, 0.05),
    ('VISA', '{"requiredFields": ["last4"], "fieldTypes": {"last4": "string"}}', 0.95, 0.03),
    ('MASTERCARD', '{"requiredFields": ["last4"], "fieldTypes": {"last4": "string"}}', 0.95, 0.03),
    ('AMEX', '{"requiredFields": ["last4"], "fieldTypes": {"last4": "string"}}', 1.01, 0.02),
    ('JCB', '{"requiredFields": ["last4"], "fieldTypes": {"last4": "string"}}', 0.95, 0.05),
    ('LINE_PAY', '{"requiredFields": [], "fieldTypes": {}}', 1.0, 0.01),
    ('PAYPAY', '{"requiredFields": [], "fieldTypes": {}}', 1.0, 0.01),
    ('POINTS', '{"requiredFields": [], "fieldTypes": {}}', 1.0, 0.0),
    ('GRAB_PAY', '{"requiredFields": [], "fieldTypes": {}}', 1.0, 0.01),
    ('BANK_TRANSFER', '{"requiredFields": ["bankDetails"], "fieldTypes": {"bankDetails": "object"}}', 1.0, 0.0),
    ('CHEQUE', '{"requiredFields": ["bankDetails"], "fieldTypes": {"bankDetails": "object"}}', 0.9, 0.0);


CREATE TABLE payments (
                          id SERIAL PRIMARY KEY,
                          customer_id VARCHAR(50) NOT NULL,
                          payment_method_id INT NOT NULL,
                          price DECIMAL(10, 2) NOT NULL,
                          final_price DECIMAL(10, 2) NOT NULL,
                          points DECIMAL(10, 2) NOT NULL,
                          additional_details JSONB,
                          datetime TIMESTAMP NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (payment_method_id) REFERENCES payment_methods (id)
);
