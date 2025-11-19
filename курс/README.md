# GameShop 
```
GameShop/курс/
├── Kursovaya/          # Frontend (Vue 3)
└── Client-server/      # Backend (Spring Boot)
```

### Запуск Frontend

```bash
cd Kursovaya
npm install
npm run dev
```

Frontend будет доступен на: http://localhost:5173

### Запуск Backend
```
Заходим в IntelijIDEA
Находим файл GameShopApplication.java по пути GameShop\курс\Client-server\src\main\java\com\gameshop
Заходим в конфигурацию запуска проекта Run/Debug Configuration
Находим в появившемся окне "+" ---> Maven и вводим следующие параметры
Name: "Spring Boot Run"
Run: "spring-boot:run"
Working Directory: "GameShop\курс\Client-server"
Profiles: "11 Java"
Далее жмём "Apply" 
И нажимаем на Зелёную стрелку
Проект запущен
```







