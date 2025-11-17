# Запуск сервера без Maven

Spring Boot приложение можно запустить без использования Maven командной строки несколькими способами:

## Вариант 1: Использование IDE (Самый простой способ)

### IntelliJ IDEA

1. **Откройте проект:**
   - File → Open → выберите папку `Client-server`

2. **Дождитесь индексации:**
   - IntelliJ автоматически распознает Maven проект и загрузит зависимости

3. **Запустите приложение:**
   - Найдите файл `src/main/java/com/gameshop/GameShopApplication.java`
   - Правой кнопкой мыши на класс → Run 'GameShopApplication.main()'
   - Или нажмите зеленую стрелку рядом с методом `main()`

### Eclipse

1. **Откройте проект:**
   - File → Import → Existing Maven Projects
   - Выберите папку `Client-server`

2. **Дождитесь загрузки зависимостей**

3. **Запустите приложение:**
   - Найдите файл `GameShopApplication.java`
   - Правой кнопкой → Run As → Java Application

### Visual Studio Code

1. **Установите расширения:**
   - Extension Pack for Java
   - Spring Boot Extension Pack

2. **Откройте папку:**
   - File → Open Folder → выберите `Client-server`

3. **Запустите:**
   - Найдите `GameShopApplication.java`
   - Нажмите F5 или Run → Start Debugging

## Вариант 2: Использование готового JAR файла

Если у вас уже есть собранный JAR файл (executable JAR):

```bash
java -jar client-server-1.0.0.jar
```

**Где взять JAR файл?**
- Попросите у разработчика, который собрал проект
- Или соберите один раз через IDE (Build → Build Project)

## Вариант 3: Использование Java напрямую (Сложный способ)

Этот способ требует ручного добавления всех зависимостей в classpath. Не рекомендуется.

## Проверка работы

После запуска приложение будет доступно на:
- **API**: http://localhost:8080/api/products
- **H2 Console**: http://localhost:8080/h2-console

Проверьте работу:
```bash
curl http://localhost:8080/api/products
```

## Требования

- **Java 17** или выше (обязательно!)
- **IDE** (IntelliJ IDEA, Eclipse, VS Code) - для Варианта 1

Проверьте версию Java:
```bash
java -version
```

## Рекомендация

**Используйте IDE (Вариант 1)** - это самый простой и надежный способ запуска Spring Boot приложения без использования Maven командной строки.

IDE автоматически:
- Загрузит все зависимости
- Скомпилирует проект
- Запустит приложение
- Покажет логи в консоли








