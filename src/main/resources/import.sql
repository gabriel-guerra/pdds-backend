INSERT INTO users (id, email, password, full_name, role) VALUES (1, 'admin@example.com', '$2a$12$O04vVop2AnOpwxEgYXYfE.mBQrC9KWMaKsELHCo3xnp7BV6XA2rJ6', 'Admin', 0);
INSERT INTO users (id, email, password, full_name, role) VALUES (2, 'user@example.com', '$2a$12$3aAThWbk5pq1//KAPIngT.1I6w1OpqjlJmsJVfZaBY4gli4INsTP2', 'User', 1);
INSERT INTO products (name, price, stock, images)
VALUES ('PlayStation 5', 400.0, 1000,
'["https://gmedia.playstation.com/is/image/SIEPDC/ps5-product-thumbnail-01-en-14sep21"]');
INSERT INTO products (name, price, stock, images)
VALUES ('PlayStation 4', 300.0, 5000,
'["https://upload.wikimedia.org/wikipedia/commons/thumb/7/71/Sony-PlayStation-4-PS4-wDualShock-4.jpg/1200px-Sony-PlayStation-4-PS4-wDualShock-4.jpg"]');
INSERT INTO products (name, price, stock, images)
VALUES ('Nintendo Switch', 250.0, 500,
'["https://upload.wikimedia.org/wikipedia/commons/thumb/7/76/Nintendo-Switch-Console-Docked-wJoyConRB.jpg/330px-Nintendo-Switch-Console-Docked-wJoyConRB.jpg"]');
INSERT INTO products (name, price, stock, images)
VALUES ('Xbox Series X', 400.0, 3500,
'["https://cms-assets.xboxservices.com/assets/68/a0/68a0e50d-0d13-42b1-8498-e55cef8a9133.png?n=642227_Hero-Gallery-0_A2_857x676.png"]');
INSERT INTO products (name, price, stock, images)
VALUES ('iPhone 15', 800.0, 200,
'["https://www.apple.com/newsroom/images/2023/09/apple-debuts-iphone-15-and-iphone-15-plus/article/Apple-iPhone-15-lineup-color-lineup-geo-230912_big.jpg.large.jpg"]');
