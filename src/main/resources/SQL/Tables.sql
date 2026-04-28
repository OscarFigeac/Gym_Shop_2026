CREATE DATABASE IF NOT EXISTS `gymshopdb`;
CREATE DATABASE IF NOT EXISTS gymshopdbtest;

-- Run in each database
# CREATE TABLE IF NOT EXISTS addresses (
#     address_id INT AUTO_INCREMENT PRIMARY KEY,
#     user_id INT NOT NULL,
#     address VARCHAR(255),
#     eircode CHAR(8) NOT NULL,
#     CONSTRAINT fk_addresses_users FOREIGN KEY(user_id) REFERENCES users(user_id)
# );

CREATE TABLE IF NOT EXISTS users(
	user_id INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(255) NOT NULL,
	full_name VARCHAR(255) NOT NULL,
	user_type ENUM('ROLE_ADMIN', 'ROLE_MEMBER') NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(72) NOT NULL,
	dob DATE NOT NULL,
    address VARCHAR(255),
    eircode CHAR(8),
    secret_key VARCHAR(255) NOT NULL,
    is_2fa_enabled BOOLEAN NOT NULL,
    stripe_customer_id VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS subscriptions(
    plan_id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    plan_name VARCHAR(255) NOT NULL,
    plan_price DECIMAL(8, 2) NOT NULL,
    plan_duration INT NOT NULL
);
CREATE TABLE IF NOT EXISTS products(
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_category VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    price DECIMAL(8, 2) NOT NULL,
    quantity INT NOT NULL
);

# CREATE TABLE IF NOT EXISTS locations(
# 	location_id INT AUTO_INCREMENT PRIMARY KEY,
# 	location_address_code VARCHAR(255) NOT NULL
# );


# CREATE TABLE IF NOT EXISTS subscriptions_users(
#     plan_id INT NOT NULL,
#     user_id INT NOT NULL,
#     expiry_date DATE NOT NULL,
#     CONSTRAINT fk_subscriptionsusers_subscriptions FOREIGN KEY(plan_id) REFERENCES subscriptions(plan_id),
#     CONSTRAINT fk_subscriptionsusers_users FOREIGN KEY(user_id) REFERENCES users(user_id)
# );

CREATE TABLE IF NOT EXISTS payment_methods(
    method_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    stripe_payment_method_id VARCHAR(255) NOT NULL,
    last_four_digits VARCHAR(4) NOT NULL,
    expiry_date VARCHAR(255) NOT NULL,
    card_type VARCHAR(12) NOT NULL,
    is_valid TINYINT NOT NULL,
    is_primary TINYINT NOT NULL,
    CONSTRAINT fk_paymentmethods_users FOREIGN KEY(user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS transactions(
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    plan_id INT NOT NULL,
    method_id INT NOT NULL,
    amount_paid DECIMAL(8, 2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    status VARCHAR(255),
    CONSTRAINT fk_transactions_users FOREIGN KEY(user_id) REFERENCES users(user_id),
    CONSTRAINT fk_transactions_subscriptions FOREIGN KEY(plan_id) REFERENCES subscriptions(plan_id),
    CONSTRAINT fk_transactions_paymentmethods FOREIGN KEY(method_id) REFERENCES payment_methods(method_id)
);

CREATE TABLE IF NOT EXISTS basket(
    basket_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_basket_users FOREIGN KEY(user_id) REFERENCES users(user_id)
);
CREATE TABLE IF NOT EXISTS basket_item(
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    basket_id INT NOT NULL,
    item_quantity INT NOT NULL,
    CONSTRAINT fk_basketItem_products FOREIGN KEY(product_id) REFERENCES products(product_id),
    CONSTRAINT fk_basketItem_basket FOREIGN KEY(basket_id) REFERENCES basket(basket_id),
    CONSTRAINT ck_basketItem_itemQuantity CHECK(item_quantity >= 0)
);
# CREATE TABLE IF NOT EXISTS productslocations(
#     product_id INT NOT NULL,
#     location_id INT NOT NULL,
#     CONSTRAINT fk_productslocationsproducts FOREIGN KEY(product_id) REFERENCES products(product_id),
#     CONSTRAINT fk_productslocationslocations FOREIGN KEY(location_id) REFERENCES locations(location_id)
# );

ALTER TABLE users ADD COLUMN stripe_customer_id VARCHAR(255);

ALTER TABLE payment_methods RENAME COLUMN processor_token TO stripe_payment_method_id;
ALTER TABLE payment_methods MODIFY COLUMN last_four_digits VARCHAR(4);