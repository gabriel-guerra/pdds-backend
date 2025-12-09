INSERT INTO users (id, email, password, full_name, role) VALUES (1, 'admin@example.com', '$2a$12$O04vVop2AnOpwxEgYXYfE.mBQrC9KWMaKsELHCo3xnp7BV6XA2rJ6', 'Admin', 0);
INSERT INTO users (id, email, password, full_name, role) VALUES (2, 'user@example.com', '$2a$12$3aAThWbk5pq1//KAPIngT.1I6w1OpqjlJmsJVfZaBY4gli4INsTP2', 'User', 1);

INSERT INTO products (id, name, price, stock) VALUES (1, 'PlayStation 5', 5000.0, 1000);
INSERT INTO product_images (product_id, images) VALUES (1, 'https://gmedia.playstation.com/is/image/SIEPDC/ps5-product-thumbnail-01-en-14sep21?$facebook$   ');
INSERT INTO product_images (product_id, images) VALUES (1, 'https://wallpapers.com/images/featured/ps5-console-png-ywbv2gv3gfw23o3w.jpg');
INSERT INTO product_images (product_id, images) VALUES (1, 'https://applebygames.co.nz/wp-content/uploads/2024/10/IMG_9391-Photoroom-Medium.png');

INSERT INTO products (id, name, price, stock) VALUES (5, 'PlayStation 4', 3000.0, 5000);
INSERT INTO product_images (product_id, images) VALUES (5, 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/Sony-PlayStation4-Pro-Console-FL.png/2560px-Sony-PlayStation4-Pro-Console-FL.png');
INSERT INTO product_images (product_id, images) VALUES (5, 'https://gmedia.playstation.com/is/image/SIEPDC/ps4-product-thumbnail-01-en-14sep21?$facebook$');
INSERT INTO product_images (product_id, images) VALUES (5, 'https://gmedia.playstation.com/is/image/SIEPDC/PS4_SLIM_Laying-down?$facebook$');

INSERT INTO products (id, name, price, stock) VALUES (2, 'Nintendo Switch 2', 5000.0, 500);
INSERT INTO product_images (product_id, images) VALUES (2, 'https://assets.nintendo.com/image/upload/f_auto/q_auto/dpr_1.5/c_scale,w_400/Marketing/68272fb37fa56918996af8bfceddbc4223c2746af7c325b2067f0c62c65ef8c0/exp_s2_e6ja3ff/hero-section/system-asset');
INSERT INTO product_images (product_id, images) VALUES (2, 'https://upload.wikimedia.org/wikipedia/commons/4/4c/Nintendo_Switch_2_in_mode_%22handheld%22.png');
INSERT INTO product_images (product_id, images) VALUES (2, 'https://images.tcdn.com.br/img/img_prod/1211726/nintendo_switch_2_edicao_padrao_1842_1_e62c35067446a22b6e077cabbae5f912.png');

INSERT INTO products (id, name, price, stock) VALUES (3, 'Xbox Series X', 4000.0, 3500);
INSERT INTO product_images (product_id, images) VALUES (3, 'https://cms-assets.xboxservices.com/assets/68/a0/68a0e50d-0d13-42b1-8498-e55cef8a9133.png?n=642227_Hero-Gallery-0_A2_857x676.png');
INSERT INTO product_images (product_id, images) VALUES (3, 'https://cms-assets.xboxservices.com/assets/bc/40/bc40fdf3-85a6-4c36-af92-dca2d36fc7e5.png?n=642227_Hero-Gallery-0_A1_857x676.png');
INSERT INTO product_images (product_id, images) VALUES (3, 'https://pngimg.com/d/xbox_PNG101378.png');

INSERT INTO products (id, name, price, stock) VALUES (4, 'iPhone 17 Pro Max', 15000.0, 200);
INSERT INTO product_images (product_id, images) VALUES (4, 'https://revendo.ch/cdn/shop/files/apple-iphone-17-pro-apple-a19-pro-3-nm-17-pro-cosmic-orange-orange-guenstig-gebraucht-kaufen-00_grande.png?v=1758094341');
INSERT INTO product_images (product_id, images) VALUES (4, 'https://www.mozillion.com/storage/product_model_images/1757672591_iPhone%2017%20Pro%20-%20Cosmic%20Orange%20-%20Side.png');
INSERT INTO product_images (product_id, images) VALUES (4, 'https://pngimg.com/uploads/iphone17/iphone17_PNG13.png');



