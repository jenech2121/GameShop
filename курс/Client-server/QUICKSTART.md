# Быстрый старт Client-server

## Запуск приложения БЕЗ Maven

### Вариант 1: Использование IDE (РЕКОМЕНДУЕТСЯ)

#### IntelliJ IDEA
1. Откройте папку `Client-server` как проект
2. Найдите `GameShopApplication.java`
3. Запустите метод `main()` (зеленая стрелка или Shift+F10)

#### Eclipse
1. File → Import → Existing Maven Projects
2. Выберите папку `Client-server`
3. Найдите `GameShopApplication.java` → Run As → Java Application

#### VS Code
1. Установите "Extension Pack for Java"
2. Откройте папку `Client-server`
3. Найдите `GameShopApplication.java` → F5

### Вариант 2: Использование готового JAR

Если у вас есть JAR файл:
```bash
java -jar client-server-1.0.0.jar
```

**Как собрать JAR через IDE:**
- **IntelliJ IDEA**: Build → Build Project, затем Build → Build Artifacts
- **Eclipse**: Run As → Maven build → Goals: `package`
- **VS Code**: Terminal → Run Task → Maven: package

## Проверка работы

После запуска откройте в браузере:
- http://localhost:8080/api/products

Должен вернуться JSON с товарами.

## Возможные проблемы

### Ошибка: "Java not found"
Установите Java 17 или выше и добавьте в PATH.

### Ошибка: "Maven not found"
**Решение:** Используйте IDE для запуска (см. Вариант 1 выше). IDE автоматически управляет зависимостями через Maven, но вам не нужно устанавливать Maven вручную.

### Порт 8080 занят
Измените порт в `src/main/resources/application.properties`:
```
server.port=8081
```

## Использование IDE

1. Откройте папку `Client-server` как Maven проект
2. Дождитесь загрузки зависимостей
3. Найдите `GameShopApplication.java`
4. Запустите метод `main()`

