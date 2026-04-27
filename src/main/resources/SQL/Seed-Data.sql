INSERT INTO products (product_category, name, price, quantity) VALUES(
    'Supplements', 'Vanilla Protein', 25.99, 12),
    ('Equipment', 'Weights(x2) 8KGS', 41.99, 35),
    ('Equipment', 'Threadmill', 89.99, 8),
    ('Supplements', 'Two-week weight loss pack', 34.99, 15),
    ('Supplements', 'Origin Pump Pre-Workout | Stim & Caffeine-Free', 23.99, 24),
    ('Equipment', 'Hex Dumbbells(x2) 55KGS', 324.99, 4),
    ('Supplements', 'Optimum Gold Whey Protein (2kg)', 50, 64.99),
    ('Supplements', 'C4 Pre-Workout - Fruit Punch', 12, 29.99),
    ('Equipment', 'Adjustable Dumbbell Set (24kg)', 4, 320.00),
    ('Equipment', 'Olympic Barbell - 20kg', 15, 180.00),
    ('Equipment', 'Resistance Band Set', 40, 19.50),
    ('Supplements', 'Creatine Monohydrate (500g)', 8, 24.99),
    ('Equipment', 'Foam Roller - High Density', 25, 15.00);

INSERT INTO users (username, fullName, userType, email, password, dob, secretKey, is2faEnabled, address, eircode) VALUES
    ('admin', 'GymShop Administrator', 'Admin', 'admin@gymshop.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.7uqqvOa', '1990-01-01', 'AL3X4ND3R', 0, '123 Iron Street', 'A65 F8P3'),
    ('OscarLove2Talk', 'John Doe', 'Member', 'john@example.com', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOAK7khz5Bde.tALTY87xI.bY7y6', '1995-05-20', 'J0HN123', 0, '45 Muscle Lane', 'D02 XY45');