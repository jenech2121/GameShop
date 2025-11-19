-- Инициализация базы данных GameShop
-- Этот файл автоматически выполняется при запуске приложения (если spring.jpa.hibernate.ddl-auto=create или create-drop)

-- Вставка тестовых товаров (если таблица пустая)
-- Примечание: ID будут автоматически сгенерированы, поэтому используем только если нужно задать конкретные ID

-- Видеокарты (GPU)
INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'NVIDIA GeForce RTX 3060', 5000.0, 'https://dlcdnwebimgs.asus.com/files/media/80edfc49-cc69-47e1-afe6-992488abbb96/img/kv/product-kv_s.jpg', 'GPU', 'Видеокарта NVIDIA GeForce RTX 3060, 12GB GDDR6', 5
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'NVIDIA GeForce RTX 3060');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'AMD Radeon RX 6700 XT', 6000.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'GPU', 'Видеокарта AMD Radeon RX 6700 XT, 12GB GDDR6', 7
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'AMD Radeon RX 6700 XT');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'NVIDIA GeForce RTX 4070', 8500.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'GPU', 'Видеокарта NVIDIA GeForce RTX 4070, 12GB GDDR6X', 3
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'NVIDIA GeForce RTX 4070');

-- Материнские платы (MOTHERBOARD)
INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'Asus Prime A520M-A', 1000.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUcMzmoQ2ysXJrw-W6Hk9_VGJlfdFXk_Ye4A&s', 'MOTHERBOARD', 'Материнская плата Asus Prime A520M-A, Socket AM4, Micro-ATX', 10
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Asus Prime A520M-A');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'Asus Prime B560M-A', 1200.0, 'https://www.tiburcc.it/img/prodimg_l/ea2b8b82b23608897103573c4458ca4d-hi.jpg', 'MOTHERBOARD', 'Материнская плата Asus Prime B560M-A, Socket LGA1200, Micro-ATX', 8
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Asus Prime B560M-A');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'MSI B450 Tomahawk Max', 1500.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUcMzmoQ2ysXJrw-W6Hk9_VGJlfdFXk_Ye4A&s', 'MOTHERBOARD', 'Материнская плата MSI B450 Tomahawk Max, Socket AM4, ATX', 6
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'MSI B450 Tomahawk Max');

-- Оперативная память (RAM)
INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'Corsair Vengeance LPX 16GB (2x8GB)', 2000.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'RAM', 'Оперативная память Corsair Vengeance LPX 16GB DDR4 3200MHz', 20
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Corsair Vengeance LPX 16GB (2x8GB)');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'Kingston Fury Beast 32GB (2x16GB)', 3500.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'RAM', 'Оперативная память Kingston Fury Beast 32GB DDR4 3200MHz', 15
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Kingston Fury Beast 32GB (2x16GB)');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'G.Skill Trident Z RGB 32GB (2x16GB)', 4000.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'RAM', 'Оперативная память G.Skill Trident Z RGB 32GB DDR4 3600MHz', 12
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'G.Skill Trident Z RGB 32GB (2x16GB)');

-- Процессоры (CPU)
INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'AMD Ryzen 5 5600X', 2500.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiblkEM2gCJ8hizpXVUwGn7mvukmKVV1CABQ&s', 'CPU', 'Процессор AMD Ryzen 5 5600X, 6 ядер, 12 потоков, 3.7-4.6 GHz', 15
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'AMD Ryzen 5 5600X');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'Intel Core i5-12400F', 3000.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQkwHI2Ii5730zSfaLgPlkg-OGhjku-Z43H6w&s', 'CPU', 'Процессор Intel Core i5-12400F, 6 ядер, 12 потоков, 2.5-4.4 GHz', 12
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Intel Core i5-12400F');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'AMD Ryzen 7 5800X', 4500.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiblkEM2gCJ8hizpXVUwGn7mvukmKVV1CABQ&s', 'CPU', 'Процессор AMD Ryzen 7 5800X, 8 ядер, 16 потоков, 3.8-4.7 GHz', 8
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'AMD Ryzen 7 5800X');

-- Блоки питания (POWER)
INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'Corsair RM750x 750W', 3500.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'POWER', 'Блок питания Corsair RM750x 750W, 80+ Gold, модульный', 10
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Corsair RM750x 750W');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'Seasonic Focus GX-650 650W', 3000.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'POWER', 'Блок питания Seasonic Focus GX-650 650W, 80+ Gold, модульный', 12
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Seasonic Focus GX-650 650W');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'be quiet! Straight Power 11 850W', 4500.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'POWER', 'Блок питания be quiet! Straight Power 11 850W, 80+ Gold, модульный', 7
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'be quiet! Straight Power 11 850W');

-- SSD накопители (SSD)
INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'Samsung 970 EVO Plus 500GB', 2500.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'SSD', 'SSD накопитель Samsung 970 EVO Plus 500GB, M.2 NVMe, 3500/3300 MB/s', 18
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Samsung 970 EVO Plus 500GB');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'WD Black SN850 1TB', 4000.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'SSD', 'SSD накопитель WD Black SN850 1TB, M.2 NVMe, 7000/5300 MB/s', 15
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'WD Black SN850 1TB');

INSERT INTO products (name, price, image, category, description, stock) 
SELECT 'Crucial MX500 1TB SATA', 2000.0, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjWYwfYaVLmN6O0IukxLaNGVHOMi-nDD_Dcg&s', 'SSD', 'SSD накопитель Crucial MX500 1TB, SATA III, 560/510 MB/s', 20
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Crucial MX500 1TB SATA');












