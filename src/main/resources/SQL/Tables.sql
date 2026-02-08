CREATE DATABASE IF NOT EXISTS `gymshopdb`;
CREATE DATABASE IF NOT EXISTS gymshopdbtest;

-- Run in each database
CREATE TABLE IF NOT EXISTS users(
	user_id INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(255) NOT NULL,
	full_name VARCHAR(255) NOT NULL,
	user_type VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(72) NOT NULL,
	dob DATE NOT NULL,
    address VARCHAR(255),
    eircode CHAR(8)
);

CREATE TABLE IF NOT EXISTS locations(
	location_id INT AUTO_INCREMENT PRIMARY KEY,
	location_addresscode VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS subscriptions(
    plan_id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    plan_name VARCHAR(255) NOT NULL,
    plan_price DECIMAL(8, 2) NOT NULL,
    plan_duration INT NOT NULL
);

CREATE TABLE IF NOT EXISTS payment_methods(
    method_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    processor_token VARCHAR(255) NOT NULL,
    expiry_date DATE NOT NULL,
    card_type VARCHAR(12) NOT NULL,
    is_valid TINYINT NOT NULL,
    is_primary TINYINT NOT NULL,
    CONSTRAINT fk_users FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS transactions(
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    plan_id INT NOT NULL,
    method_id INT NOT NULL,
    amount_paid DECIMAL(8, 2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    status VARCHAR(255),
    CONSTRAINT fk_users FOREIGN KEY(user_id) REFERENCES users(user_id),
    CONSTRAINT fk_subscriptions FOREIGN KEY(plan_id) REFERENCES subscriptions(plan_id),
    CONSTRAINT fk_paymentmethods FOREIGN KEY(method_id) REFERENCES payment_methods(method_id)
);

CREATE TABLE IF NOT EXISTS products(
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_category VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    price DECIMAL(8, 2) NOT NULL,
    in_stock TINYINT NOT NULL,
    location_id INT,
    CONSTRAINT fk_locations FOREIGN KEY(location_id) REFERENCES locations(location_id)
);

CREATE TABLE IF NOT EXISTS productslocations(
    product_id INT NOT NULL,
    location_id INT NOT NULL,
    CONSTRAINT fk_products FOREIGN KEY(product_id) REFERENCES products(product_id),
    CONSTRAINT fk_locations FOREIGN KEY(location_id) REFERENCES locations(location_id)
);