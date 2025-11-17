# Структура базы данных GameShop

## Описание

База данных состоит из 5 основных таблиц:
1. **users** - Пользователи
2. **products** - Товары
3. **cart_items** - Корзина покупок
4. **orders** - Заказы
5. **order_items** - Элементы заказа

## Схема базы данных

### 1. Таблица `users` (Пользователи)

| Поле | Тип | Описание |
|------|-----|----------|
| id | BIGINT | Уникальный идентификатор (Primary Key, Auto Increment) |
| email | VARCHAR(255) | Email пользователя (Unique, Not Null) |
| password | VARCHAR(255) | Пароль пользователя (Not Null) |
| created_at | TIMESTAMP | Дата регистрации (по умолчанию текущая дата) |

**Связи:**
- Один пользователь может иметь много элементов корзины (`cart_items`)
- Один пользователь может иметь много заказов (`orders`)

### 2. Таблица `products` (Товары)

| Поле | Тип | Описание |
|------|-----|----------|
| id | BIGINT | Уникальный идентификатор (Primary Key, Auto Increment) |
| name | VARCHAR(255) | Название товара (Not Null) |
| price | DOUBLE | Цена товара (Not Null) |
| image | VARCHAR(1000) | URL изображения товара |
| category | VARCHAR(50) | Категория товара (Not Null) |
| description | VARCHAR(2000) | Описание товара |
| stock | INTEGER | Количество на складе (Not Null, Default: 0) |

**Категории товаров:**
- `GPU` - Видеокарта
- `MOTHERBOARD` - Материнская Плата
- `RAM` - Оперативная память
- `CPU` - Процессор
- `POWER` - Блок питания
- `SSD` - SSD накопитель

**Связи:**
- Один товар может быть в нескольких элементах корзины (`cart_items`)
- Один товар может быть в нескольких элементах заказа (`order_items`)

**Индексы:**
- `idx_products_category` - для быстрого поиска по категории

### 3. Таблица `cart_items` (Корзина покупок)

| Поле | Тип | Описание |
|------|-----|----------|
| id | BIGINT | Уникальный идентификатор (Primary Key, Auto Increment) |
| user_id | BIGINT | ID пользователя (Foreign Key → users.id, Not Null) |
| product_id | BIGINT | ID товара (Foreign Key → products.id, Not Null) |
| quantity | INTEGER | Количество товара (Not Null, Default: 1) |

**Связи:**
- Многие элементы корзины принадлежат одному пользователю (`users`)
- Многие элементы корзины ссылаются на один товар (`products`)

**Индексы:**
- `idx_cart_items_user` - для быстрого поиска корзины пользователя

### 4. Таблица `orders` (Заказы)

| Поле | Тип | Описание |
|------|-----|----------|
| id | BIGINT | Уникальный идентификатор (Primary Key, Auto Increment) |
| user_id | BIGINT | ID пользователя (Foreign Key → users.id, Not Null) |
| total_price | DOUBLE | Общая стоимость заказа (Not Null) |
| status | VARCHAR(50) | Статус заказа (Not Null, Default: 'PENDING') |
| created_at | TIMESTAMP | Дата создания заказа (по умолчанию текущая дата) |

**Статусы заказа:**
- `PENDING` - Ожидает обработки
- `PROCESSING` - В обработке
- `SHIPPED` - Отправлен
- `DELIVERED` - Доставлен
- `CANCELLED` - Отменен

**Связи:**
- Один заказ принадлежит одному пользователю (`users`)
- Один заказ содержит много элементов заказа (`order_items`)

**Индексы:**
- `idx_orders_user` - для быстрого поиска заказов пользователя

### 5. Таблица `order_items` (Элементы заказа)

| Поле | Тип | Описание |
|------|-----|----------|
| id | BIGINT | Уникальный идентификатор (Primary Key, Auto Increment) |
| order_id | BIGINT | ID заказа (Foreign Key → orders.id, Not Null) |
| product_id | BIGINT | ID товара (Foreign Key → products.id, Not Null) |
| quantity | INTEGER | Количество товара (Not Null) |
| price | DOUBLE | Цена товара на момент заказа (Not Null) |

**Связи:**
- Многие элементы заказа принадлежат одному заказу (`orders`)
- Многие элементы заказа ссылаются на один товар (`products`)

**Индексы:**
- `idx_order_items_order` - для быстрого поиска элементов заказа

## Диаграмма связей

```
users (1) ────< (N) cart_items (N) >─── (1) products
  │
  │ (1)
  │
  └───< (N) orders (1) >─── (N) order_items (N) >─── (1) products
```

## Инициализация данных

При первом запуске приложения автоматически создаются:

### Товары (18 товаров по 3 в каждой категории):

**Видеокарты (GPU):**
- NVIDIA GeForce RTX 3060 - 5000₽
- AMD Radeon RX 6700 XT - 6000₽
- NVIDIA GeForce RTX 4070 - 8500₽

**Материнские платы (MOTHERBOARD):**
- Asus Prime A520M-A - 1000₽
- Asus Prime B560M-A - 1200₽
- MSI B450 Tomahawk Max - 1500₽

**Оперативная память (RAM):**
- Corsair Vengeance LPX 16GB - 2000₽
- Kingston Fury Beast 32GB - 3500₽
- G.Skill Trident Z RGB 32GB - 4000₽

**Процессоры (CPU):**
- AMD Ryzen 5 5600X - 2500₽
- Intel Core i5-12400F - 3000₽
- AMD Ryzen 7 5800X - 4500₽

**Блоки питания (POWER):**
- Corsair RM750x 750W - 3500₽
- Seasonic Focus GX-650 650W - 3000₽
- be quiet! Straight Power 11 850W - 4500₽

**SSD накопители (SSD):**
- Samsung 970 EVO Plus 500GB - 2500₽
- WD Black SN850 1TB - 4000₽
- Crucial MX500 1TB SATA - 2000₽

## SQL файлы

- `schema.sql` - Создание структуры таблиц (выполняется автоматически через Hibernate)
- `data.sql` - Инициализация тестовых данных (выполняется через DataInitializer.java)

## Доступ к базе данных

### H2 Console (в браузере)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:gameshopdb`
- Username: `sa`
- Password: (пусто)

### Примеры SQL запросов

**Получить все товары:**
```sql
SELECT * FROM products;
```

**Получить товары по категории:**
```sql
SELECT * FROM products WHERE category = 'GPU';
```

**Получить корзину пользователя:**
```sql
SELECT ci.*, p.name, p.price 
FROM cart_items ci 
JOIN products p ON ci.product_id = p.id 
WHERE ci.user_id = 1;
```

**Получить заказы пользователя:**
```sql
SELECT o.*, COUNT(oi.id) as items_count 
FROM orders o 
LEFT JOIN order_items oi ON o.id = oi.order_id 
WHERE o.user_id = 1 
GROUP BY o.id;
```








