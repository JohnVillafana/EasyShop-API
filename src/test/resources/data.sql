-- Clear existing categories
DELETE FROM categories;

-- Insert exactly 18 categories
INSERT INTO categories (category_id, name, description) VALUES
(1, 'Electronics', 'Electronic devices and accessories'),
(2, 'Clothing', 'Clothing and apparel'),
(3, 'Books', 'Books and magazines'),
(4, 'Sports', 'Sports equipment and gear'),
(5, 'Home & Garden', 'Home and garden items'),
(6, 'Toys', 'Toys and games for children'),
(7, 'Beauty', 'Beauty and personal care products'),
(8, 'Automotive', 'Car parts and accessories'),
(9, 'Food & Grocery', 'Food and grocery items'),
(10, 'Health', 'Health and wellness products'),
(11, 'Music', 'Musical instruments and accessories'),
(12, 'Movies', 'Movies and entertainment media'),
(13, 'Games', 'Video games and board games'),
(14, 'Baby', 'Baby and toddler products'),
(15, 'Office', 'Office supplies and equipment'),
(16, 'Pet Supplies', 'Pet food and accessories'),
(17, 'Tools', 'Tools and hardware'),
(18, 'Jewelry', 'Jewelry and watches');

-- Reset auto-increment
ALTER TABLE categories AUTO_INCREMENT = 19;