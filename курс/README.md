# GameShop - Интернет-магазин компьютерных комплектующих

Полнофункциональное веб-приложение для интернет-магазина компьютерных комплектующих с раздельной архитектурой фронтенда и бэкенда.

## Структура проекта

Проект разделен на две независимые части для отдельного запуска:

```
GameShop/курс/
├── Kursovaya/          # Frontend (Vue 3)
└── Client-server/      # Backend (Spring Boot)
```

## Технологический стек

### Frontend (Kursovaya)
- **Vue 3** - JavaScript framework
- **Vite** - Build tool
- **Vue Router** - Маршрутизация
- **Tailwind CSS** - Стилизация
- **TypeScript** - Типизация

### Backend (Client-server)
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (разработка) / **PostgreSQL** (продакшен)
- **Maven**

## Быстрый старт

### Запуск Frontend

```bash
cd Kursovaya
npm install
npm run dev
```

Frontend будет доступен на: http://localhost:5173

### Запуск Backend

```bash
cd Client-server
mvn clean install
mvn spring-boot:run
```

Backend будет доступен на: http://localhost:8080

## Функциональность

### Реализовано

#### Frontend
- ✅ Главная страница с информацией о магазине
- ✅ Каталог товаров (материнские платы, процессоры, видеокарты, ОЗУ)
- ✅ Корзина покупок
- ✅ Личный кабинет (форма входа)
- ✅ Страницы "О нас" и "Контакты"
- ✅ Хранение данных в localStorage

#### Backend
- ✅ REST API для товаров (CRUD операции)
- ✅ REST API для пользователей (регистрация, вход)
- ✅ REST API для корзины
- ✅ REST API для заказов
- ✅ База данных с автоматической инициализацией тестовых данных
- ✅ CORS настройка для работы с фронтендом

### API Endpoints

#### Товары
- `GET /api/products` - Все товары
- `GET /api/products/{id}` - Товар по ID
- `GET /api/products/category/{category}` - Товары по категории
- `GET /api/products/search?name={name}` - Поиск товаров

#### Пользователи
- `POST /api/users/register` - Регистрация
- `POST /api/users/login` - Вход

#### Корзина
- `GET /api/cart/{userId}` - Получить корзину
- `POST /api/cart/add` - Добавить в корзину
- `DELETE /api/cart/{userId}/{cartItemId}` - Удалить из корзины

#### Заказы
- `POST /api/orders/{userId}` - Создать заказ
- `GET /api/orders/{userId}` - Заказы пользователя

## Документация

Подробная документация для каждой части проекта:

- [Frontend README](Kursovaya/README.md)
- [Backend README](Client-server/README.md)

## Разработка

### Интеграция Frontend и Backend

Для полной интеграции необходимо:

1. Создать API клиент во фронтенде для работы с REST API
2. Заменить localStorage на запросы к бэкенду
3. Реализовать авторизацию через JWT токены (опционально)

### База данных

По умолчанию используется H2 in-memory база данных. Для доступа к консоли:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:gameshopdb`
- Username: `sa`
- Password: (пусто)

## Примечания

- Backend и Frontend могут запускаться независимо
- CORS настроен для работы с фронтендом на портах 5173 и 3000
- При первом запуске бэкенда автоматически создаются тестовые товары
- Для продакшена рекомендуется использовать PostgreSQL и настроить безопасность (хеширование паролей, JWT токены)









