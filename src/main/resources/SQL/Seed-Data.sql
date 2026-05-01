DELETE FROM products;

INSERT INTO products (product_category, name, description, price, quantity, image_url) VALUES
('Supplements', 'Vanilla Protein', 'Premium whey protein isolate, perfect for post-workout recovery and muscle growth.', 25.99, 12, '/images/products/vanilla-protein.jpg'),
('Equipment', 'Weights(x2) 8KGS', 'Durable, easy-grip 8kg dumbbells. Ideal for a wide range of strength training exercises.', 41.99, 35, '/images/products/8kgs-weights.jpg'),
('Equipment', 'Threadmill', 'High-performance treadmill with built-in workout programs and heart rate monitoring.', 89.99, 8, '/images/products/treadmill.jpg'),
('Supplements', 'Two-week weight loss pack', 'A comprehensive 14-day supplement kit designed to boost metabolism and support fat loss.', 34.99, 15, '/images/products/2-week-weightloss.jpg'),
('Supplements', 'Origin Pump Pre-Workout', 'Caffeine-free formula designed to maximize blood flow and muscle pump during training.', 23.99, 24, '/images/products/origin-pump.jpg'),
('Equipment', 'Hex Dumbbells(x2) 55KGS', 'Heavy-duty professional hex dumbbells for advanced strength training and powerlifting.', 324.99, 4, '/images/products/hex-55kg.jpg'),
('Supplements', 'Optimum Gold Whey Protein (2kg)', 'The world''s best-selling whey protein, providing 24g of protein per serving.', 50.00, 64, '/images/products/optimum-nutrition.jpg'),
('Supplements', 'C4 Pre-Workout - Fruit Punch', 'Explosive energy and focus supplement to help you push through your toughest sets.', 12.00, 29, '/images/products/c4-fruity.jpg'),
('Equipment', 'Adjustable Dumbbell Set (24kg)', 'Versatile space-saving dumbbells that allow you to adjust weight from 2kg to 24kg instantly.', 320.00, 4, '/images/products/adjustable-dumbbell.jpg'),
('Equipment', 'Olympic Barbell - 20kg', 'Standard 7ft Olympic bar made from high-tensile steel with a chrome finish.', 180.00, 15, '/images/products/olympic-bar.jpg'),
('Equipment', 'Resistance Band Set', 'Set of 5 heavy-duty bands with different resistance levels for full-body workouts.', 19.50, 40, '/images/products/resistance-bands.jpg'),
('Supplements', 'Creatine Monohydrate (500g)', '100% pure micronized creatine monohydrate to increase physical performance.', 24.99, 8, '/images/products/creatine.jpg'),
('Equipment', 'Foam Roller - High Density', 'Essential recovery tool for self-myofascial release and relieving muscle tension.', 15.00, 25, '/images/products/foam-roller.jpg');

INSERT INTO users (username, full_name, user_type, email, password, dob, secret_key, is_2fa_enabled, address, eircode) VALUES
    ('admin', 'GymShop Administrator', 'ROLE_ADMIN', 'admin@gymshop.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.7uqqvOa', '1990-01-01', 'AL3X4ND3R', 0, '123 Iron Street', 'A65 F8P3'),
    ('OscarLove2Talk', 'John Doe', 'ROLE_MEMBER', 'john@example.com', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOAK7khz5Bde.tALTY87xI.bY7y6', '1995-05-20', 'J0HN123', 0, '45 Muscle Lane', 'D02 XY45');