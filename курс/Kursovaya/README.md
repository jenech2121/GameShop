# GameShop Frontend (Kursovaya)

Фронтенд приложение для интернет-магазина компьютерных комплектующих GameShop, разработанное на Vue 3.

## Технологии

- **Vue 3** - Progressive JavaScript Framework
- **Vite** - Build tool и dev server
- **Vue Router** - Маршрутизация
- **Tailwind CSS** - Utility-first CSS framework
- **TypeScript** - Типизация

## Структура проекта

```
Kursovaya/
├── src/
│   ├── assets/          # Статические ресурсы (изображения, стили)
│   ├── components/       # Vue компоненты
│   │   ├── composition/ # Composition API функции
│   │   └── icons/       # Иконки
│   ├── pages/           # Страницы приложения
│   ├── router/          # Конфигурация маршрутизации
│   ├── App.vue          # Главный компонент
│   └── main.js          # Точка входа
├── public/              # Публичные файлы
├── package.json
└── vite.config.js
```

## Страницы приложения

- **Главная** (`/`) - Главная страница с информацией о магазине
- **Каталог** (`/catalog`) - Каталог товаров (материнские платы, процессоры, видеокарты, ОЗУ)
- **Корзина** (`/Corzina`) - Корзина покупок
- **Личный кабинет** (`/cabinet`) - Вход и регистрация
- **О нас** (`/about`) - Информация о компании
- **Контакты** (`/contacts`) - Контактная информация

## Установка и запуск

### Требования
- Node.js 16+ 
- npm или yarn

### Установка зависимостей

```bash
npm install
```

### Запуск в режиме разработки

```bash
npm run dev
```

Приложение будет доступно по адресу: http://localhost:5173

### Сборка для продакшена

```bash
npm run build
```

### Предпросмотр продакшен сборки

```bash
npm run preview
```

## Интеграция с бэкендом

Для работы с бэкенд API необходимо:

1. Убедиться, что бэкенд запущен на порту 8080 (см. README в папке Client-server)
2. Настроить API endpoints в компонентах для работы с REST API

### Пример интеграции

Создайте файл `src/api/api.js`:

```javascript
const API_BASE_URL = 'http://localhost:8080/api';

export const productAPI = {
  getAll: () => fetch(`${API_BASE_URL}/products`).then(r => r.json()),
  getById: (id) => fetch(`${API_BASE_URL}/products/${id}`).then(r => r.json()),
  // ... другие методы
};
```

## Функциональность

### Текущая реализация
- Просмотр каталога товаров
- Добавление товаров в корзину
- Управление корзиной (добавление/удаление)
- Форма входа в личный кабинет
- Хранение данных в localStorage

### Планируемая интеграция с бэкендом
- Получение товаров с сервера
- Авторизация через API
- Синхронизация корзины с сервером
- Создание заказов через API

## Рекомендуемые IDE

- [VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (отключите Vetur)

## Дополнительная информация

См. [Vite Configuration Reference](https://vite.dev/config/) для настройки проекта.
