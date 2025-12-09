INSERT INTO users (id, email, password, full_name, role) VALUES (1, 'admin@example.com', '$2a$12$O04vVop2AnOpwxEgYXYfE.mBQrC9KWMaKsELHCo3xnp7BV6XA2rJ6', 'Admin', 0);
INSERT INTO users (id, email, password, full_name, role) VALUES (2, 'user@example.com', '$2a$12$3aAThWbk5pq1//KAPIngT.1I6w1OpqjlJmsJVfZaBY4gli4INsTP2', 'User', 1);

INSERT INTO products (id, name, price, stock) VALUES (1, 'PlayStation 5', 5000.0, 1000);
INSERT INTO product_images (product_id, images) VALUES (1, 'https://gmedia.playstation.com/is/image/SIEPDC/ps5-product-thumbnail-01-en-14sep21');
INSERT INTO product_images (product_id, images) VALUES (1, 'https://t4.ftcdn.net/jpg/04/25/75/97/360_F_425759722_cuKo0zk3AnqsjyKcMtj1NfI4i78L1KtU.jpg');
INSERT INTO product_images (product_id, images) VALUES (1, 'https://m.media-amazon.com/images/I/51+qnZm7V7L.jpg');

INSERT INTO products (id, name, price, stock) VALUES (5, 'PlayStation 4', 3000.0, 5000);
INSERT INTO product_images (product_id, images) VALUES (5, 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/71/Sony-PlayStation-4-PS4-wDualShock-4.jpg/1200px-Sony-PlayStation-4-PS4-wDualShock-4.jpg');
INSERT INTO product_images (product_id, images) VALUES (5, 'https://i.zst.com.br/thumbs/12/2c/3f/165939772.jpg');
INSERT INTO product_images (product_id, images) VALUES (5, 'https://gmedia.playstation.com/is/image/SIEPDC/ps4-product-thumbnail-01-en-14sep21?$facebook$');

INSERT INTO products (id, name, price, stock) VALUES (2, 'Nintendo Switch', 2500.0, 500);
INSERT INTO product_images (product_id, images) VALUES (2, 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/76/Nintendo-Switch-Console-Docked-wJoyConRB.jpg/330px-Nintendo-Switch-Console-Docked-wJoyConRB.jpg');
INSERT INTO product_images (product_id, images) VALUES (2, 'https://assets.nintendo.com/image/upload/f_auto/q_auto/c_fill,w_300/ncom/en_US/switch/system/three-modes-in-one');
INSERT INTO product_images (product_id, images) VALUES (2, 'https://m.media-amazon.com/images/I/51YXZgm0DbL.jpg');

INSERT INTO products (id, name, price, stock) VALUES (3, 'Xbox Series X', 4000.0, 3500);
INSERT INTO product_images (product_id, images) VALUES (3, 'https://cms-assets.xboxservices.com/assets/68/a0/68a0e50d-0d13-42b1-8498-e55cef8a9133.png?n=642227_Hero-Gallery-0_A2_857x676.png');
INSERT INTO product_images (product_id, images) VALUES (3, 'https://m.media-amazon.com/images/I/41wbBaMnh2L._AC_UF1000,1000_QL80_.jpg');
INSERT INTO product_images (product_id, images) VALUES (3, 'https://cms-assets.xboxservices.com/assets/ec/f6/ecf6f6a6-6048-40db-9171-cc62813ffe97.jpg?n=Xbox-Series-X_Super-Hero-1400_Complete-Control_1920x1400_01.jpg');

INSERT INTO products (id, name, price, stock) VALUES (4, 'iPhone 17 Pro Max', 15000.0, 200);
INSERT INTO product_images (product_id, images) VALUES (4, 'https://s2-techtudo.glbimg.com/z8tNhlZ95y0eN7vYKP4yB3fZLjo=/0x0:1600x900/888x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2025/c/S/golBBQTUyBDRAhiApHtQ/iphone-17-laranja.jpeg');
INSERT INTO product_images (product_id, images) VALUES (4, 'https://horizonplay.fbitsstatic.net/img/p/apple-iphone-17-pro-max-256gb-12ram-tela-super-retina-xdr-oled-6-9-laranja-cosmico-193478/380177-2.jpg?w=670&h=670&v=202511152237');
INSERT INTO product_images (product_id, images) VALUES (4, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMJoR9AQ6IL7dScDiadc2OXes8c8qH2cVaVA&s');



