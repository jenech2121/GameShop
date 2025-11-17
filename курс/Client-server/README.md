# GameShop Backend (Client-server)

Backend API для интернет-магазина компьютерных комплектующих GameShop, разработанный на Java Spring Boot.

## Технологии

- **Java 11**
- **Spring Boot 2.7.18**
- **Spring Data JPA**
- **Spring Security** с JWT аутентификацией
- **H2 Database** (для разработки)
- **PostgreSQL** (для продакшена)
- **Maven**

## Структура проекта

```
Client-server/
├── src/
│   ├── main/
│   │   ├── java/com/gameshop/
│   │   │   ├── config/          # Конфигурация (CORS, Security)
│   │   │   ├── controller/      # REST контроллеры
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── model/           # JPA сущности
│   │   │   ├── repository/      # JPA репозитории
│   │   │   ├── service/         # Бизнес-логика
│   │   │   └── GameShopApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## API Endpoints

### Товары (Products)
- `GET /api/products` - Получить все товары
- `GET /api/products/{id}` - Получить товар по ID
- `GET /api/products/category/{category}` - Получить товары по категории
- `GET /api/products/search?name={name}` - Поиск товаров
- `POST /api/products` - Создать товар
- `PUT /api/products/{id}` - Обновить товар
- `DELETE /api/products/{id}` - Удалить товар

### Пользователи (Users)
- `POST /api/users/register` - Регистрация пользователя (возвращает JWT токен)
- `POST /api/users/login` - Вход в систему (возвращает JWT токен)
- `GET /api/users/{id}` - Получить пользователя по ID

### Админ (Admin) - требует JWT токен с ролью ADMIN
- `GET /api/admin/users` - Получить всех пользователей
- `GET /api/admin/users/{id}` - Получить пользователя по ID
- `PUT /api/admin/users/{id}/status` - Изменить статус пользователя (ACTIVE/BLOCKED)
- `PUT /api/admin/users/{id}/role` - Изменить роль пользователя (USER/ADMIN)
- `POST /api/admin/products` - Создать товар
- `PUT /api/admin/products/{id}` - Обновить товар
- `PUT /api/admin/products/{id}/image` - Изменить изображение товара
- `DELETE /api/admin/products/{id}` - Удалить товар

### Корзина (Cart)
- `GET /api/cart/{userId}` - Получить товары в корзине
- `POST /api/cart/add` - Добавить товар в корзину
- `DELETE /api/cart/{userId}/{cartItemId}` - Удалить товар из корзины
- `DELETE /api/cart/{userId}/clear` - Очистить корзину

### Заказы (Orders)
- `POST /api/orders/{userId}` - Создать заказ (JSON с `paymentMethod`, `paymentType`, `deliveryAddress`, `cardNumber`, `cardExpiry`, `cardCvv`)
- `GET /api/orders/{userId}` - Получить заказы пользователя
- `GET /api/admin/orders` - Все заказы (доступно админу)
- `GET /api/admin/users/{id}/orders` - Заказы конкретного пользователя (доступно админу)

## Запуск приложения

### Требования
- **Java 11** или выше (обязательно!)
- **Maven** (встроен в IntelliJ IDEA, не требует отдельной установки)

⚠️ **Важно:** Ошибка `UnsupportedClassVersionError` означает, что используется слишком старая версия Java (например, 8). 
Установите Java 11 и настройте IntelliJ IDEA (см. файл `FIX_JAVA_VERSION.md`).

### Способ 1: Через Maven в IntelliJ IDEA (РЕКОМЕНДУЕТСЯ)

**Шаг 1: Откройте проект**
1. File → Open → выберите папку `Client-server`
2. Дождитесь индексации (IntelliJ автоматически распознает Maven проект)

**Шаг 2: Настройте Java 11**
1. File → Project Structure (Ctrl+Alt+Shift+S)
2. Project → Project SDK: выберите Java 11 (если нет - добавьте через "Add SDK" → Download JDK)
3. Project → Project language level: 11
4. OK

**Шаг 3: Запустите через Maven**
1. Откройте вкладку **Maven** справа (или View → Tool Windows → Maven)
2. Разверните проект → Plugins → spring-boot
3. Дважды кликните на **spring-boot:run**
4. Или в терминале IntelliJ IDEA выполните: `mvn spring-boot:run`

**Альтернативный способ через Run Configuration:**
1. Run → Edit Configurations
2. Нажмите "+" → Maven
3. Name: `Spring Boot Run`
4. Working directory: `$PROJECT_DIR$/Client-server` (или путь к папке Client-server)
5. Command line: `spring-boot:run`
6. JRE: выберите Java 11
7. OK
8. Запустите через Run → Run 'Spring Boot Run' (или Shift+F10)

### Способ 2: Через метод main() в IntelliJ IDEA

1. Найдите `src/main/java/com/gameshop/GameShopApplication.java`
2. **Настройте Java 11:**
   - File → Project Structure (Ctrl+Alt+Shift+S)
   - Project → Project SDK: Java 11
   - Project → Project language level: 11
   - OK
3. **Настройте Run Configuration:**
   - Run → Edit Configurations
   - Найдите или создайте конфигурацию для `GameShopApplication`
   - JRE: выберите Java 11
   - OK
4. Запустите метод `main()` (зеленая стрелка или Shift+F10)

**Eclipse:**
1. File → Import → Existing Maven Projects
2. Выберите папку `Client-server`
3. Дождитесь загрузки зависимостей
4. Найдите `GameShopApplication.java` → Run As → Java Application

**VS Code:**
1. Установите "Extension Pack for Java"
2. Откройте папку `Client-server`
3. Дождитесь индексации
4. Найдите `GameShopApplication.java` → F5

### Способ 3: Через командную строку (если Maven установлен)

Если у вас установлен Maven в системе:
```bash
cd Client-server
mvn spring-boot:run
```

### Способ 4: Через готовый JAR файл

**Сборка JAR:**
- В Maven панели: Plugins → spring-boot → spring-boot:repackage
- Или в терминале: `mvn clean package`
- JAR будет в папке `target/client-server-1.0.0.jar`

**Запуск JAR:**
```bash
java -jar target/client-server-1.0.0.jar
```

**Подробные инструкции по Maven в IntelliJ IDEA:** см. файл `MAVEN_INTELLIJ.md`

### Проверка работы

После запуска приложение будет доступно на:
- **API**: http://localhost:8080/api/products
- **H2 Console**: http://localhost:8080/h2-console

Проверьте работу API:
```bash
curl http://localhost:8080/api/products
```

Должен вернуться JSON с товарами.

### Настройка базы данных

По умолчанию используется H2 in-memory база данных. Для доступа к консоли H2:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:gameshopdb`
- Username: `sa`
- Password: (пусто)

Для использования PostgreSQL измените `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gameshopdb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Примечания

- **Maven не требуется устанавливать отдельно** - IDE использует встроенный Maven
- Все зависимости загружаются автоматически при открытии проекта в IDE
- Для сборки JAR файла используйте встроенные инструменты IDE

## Порт

Сервер запускается на порту **8080** по умолчанию.

## CORS

Настроен CORS для работы с фронтендом на:
- http://localhost:5173 (Vite)
- http://localhost:3000

## Примеры запросов

### Регистрация пользователя
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

Ответ:
```json
{
  "user": {
    "id": 1,
    "email": "user@example.com",
    "role": "USER",
    "status": "ACTIVE"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Вход в систему
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

### Вход как администратор
По умолчанию создается администратор:
- Email: `admin@gmail.com`
- Password: `admin`

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@gmail.com","password":"admin"}'
```

### Использование JWT токена
Все защищенные эндпоинты требуют заголовок Authorization:
```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Админские функции (примеры)

#### Получить всех пользователей
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

#### Изменить статус пользователя
```bash
curl -X PUT http://localhost:8080/api/admin/users/1/status \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"BLOCKED"}'
```

#### Изменить изображение товара
```bash
curl -X PUT http://localhost:8080/api/admin/products/1/image \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"image":"https://example.com/new-image.jpg"}'
```

#### Создать товар
```bash
curl -X POST http://localhost:8080/api/admin/products \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "NVIDIA GeForce RTX 4090",
    "price": 15000.0,
    "image": "https://example.com/image.jpg",
    "category": "GPU",
    "description": "Мощная видеокарта",
    "stock": 5
  }'
```

### Получение всех товаров
```bash
curl http://localhost:8080/api/products
```

### Добавление товара в корзину
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"productId":1,"quantity":1}'
```

## Система аутентификации

- **JWT токены**: Используются для аутентификации пользователей
- **Роли**: USER (обычный пользователь) и ADMIN (администратор)
- **Статусы пользователей**: ACTIVE (активен) и BLOCKED (заблокирован)
- **Хеширование паролей**: Используется BCrypt для безопасного хранения паролей
- **Токены действительны**: 24 часа (настраивается в application.properties)

## Примечания

- При первом запуске автоматически создаются:
  - Администратор: `admin@gmail.com` / `admin`
  - Тестовые товары
- Все защищенные эндпоинты требуют JWT токен в заголовке `Authorization: Bearer <token>`
- Публичные эндпоинты: регистрация, вход, получение списка товаров

