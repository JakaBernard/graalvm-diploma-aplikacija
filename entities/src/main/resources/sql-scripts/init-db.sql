-- products
INSERT INTO product (product_name, price) VALUES ('USB 8GB', 10); -- 1
INSERT INTO product (product_name, price) VALUES ('USB 16GB', 18); -- 2
INSERT INTO product (product_name, price) VALUES ('USB 32GB', 25);  -- 3
INSERT INTO product (product_name, price) VALUES ('HDMI Cable 1m', 5); -- 4
INSERT INTO product (product_name, price) VALUES ('HDMI Cable 1.5m', 7); -- 5
INSERT INTO product (product_name, price) VALUES ('HDMI Cable 2m', 10); -- 6
INSERT INTO product (product_name, price) VALUES ('HDMI Cable 3m', 15); -- 7
INSERT INTO product (product_name, price) VALUES ('HDMI Cable 5m', 20); -- 8


-- reward
INSERT INTO reward (reward_name, price) VALUES ('Free coffee', 5); -- 1 
INSERT INTO reward (reward_name, price) VALUES ('Dry-erase marker', 10); -- 2 
INSERT INTO reward (reward_name, price) VALUES ('3h F.U.N. Room voucher', 20); -- 3

-- purchases
INSERT INTO purchase (product_id, amount) VALUES (1, 3); -- 3x USB 8GB
INSERT INTO purchase (product_id, amount) VALUES (3, 1); -- 1x USB 32GB
INSERT INTO purchase (product_id, amount) VALUES (3, 3); -- 3x USB 32GB
INSERT INTO purchase (product_id, amount) VALUES (2, 14); -- 14x USB 16GB
INSERT INTO purchase (product_id, amount) VALUES (6, 5); -- 5x HDMI 2m

-- claims
INSERT INTO claim (reward_id, amount) VALUES (1, 5) -- 5x Free coffee
INSERT INTO claim (reward_id, amount) VALUES (2, 2) -- 5x Dry-erase marker

