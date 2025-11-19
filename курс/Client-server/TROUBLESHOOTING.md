# Решение проблем с запуском приложения

## Рекомендация: Используйте IDE

**Самый простой способ** - использовать IDE (IntelliJ IDEA, Eclipse, VS Code). IDE автоматически управляет зависимостями через встроенный Maven, вам не нужно устанавливать Maven отдельно.

## Проблема: Приложение не запускается

### Возможные причины и решения:

#### 1. Используйте IDE вместо Maven командной строки

**Рекомендация:** Не устанавливайте Maven отдельно. Используйте IDE, которая имеет встроенный Maven:

- **IntelliJ IDEA** - имеет встроенный Maven
- **Eclipse** - имеет встроенный Maven
- **VS Code** - использует расширения Java, которые управляют Maven

Если вы все же хотите использовать Maven командной строки, установите его с https://maven.apache.org/download.cgi

#### 2. Java не установлена или не в PATH

**Проверка:**
```bash
java -version
```

Должна быть версия Java 17 или выше.

**Если Java не установлена:**
- Скачайте Java 17+ с https://adoptium.net/
- Установите и добавьте в PATH

#### 3. Проблемы с pom.xml

**Исправлено:** Удален некорректный тег из pom.xml.

**Если используете IDE:**
- IDE автоматически проверит pom.xml при открытии проекта
- Если есть ошибки, они будут показаны в IDE

#### 4. Проблемы с зависимостями в IDE

**IntelliJ IDEA:**
- File → Invalidate Caches / Restart
- Maven → Reload Project

**Eclipse:**
- Правой кнопкой на проект → Maven → Update Project
- Выберите "Force Update of Snapshots/Releases"

**VS Code:**
- Command Palette (Ctrl+Shift+P) → Java: Clean Java Language Server Workspace

#### 5. Порт 8080 занят

**Проверка:**
```bash
# Windows
netstat -ano | findstr :8080

# Linux/Mac
lsof -i :8080
```

**Решение:** Измените порт в `src/main/resources/application.properties`:
```properties
server.port=8081
```

#### 6. Проблемы с кодировкой пути (Windows)

Если путь содержит кириллицу, могут быть проблемы. Попробуйте:
- Переместить проект в путь без кириллицы (например, `C:\projects\GameShop`)
- Или использовать IDE для запуска

## Способы запуска

### Вариант 1: Через IDE (РЕКОМЕНДУЕТСЯ)

**IntelliJ IDEA:**
1. File → Open → выберите папку `Client-server`
2. Дождитесь индексации (IDE автоматически загрузит зависимости)
3. Найдите `GameShopApplication.java`
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

### Вариант 2: Собрать JAR через IDE и запустить

**Сборка JAR:**
- **IntelliJ IDEA**: Build → Build Project, затем Build → Build Artifacts
- **Eclipse**: Run As → Maven build → Goals: `package`
- **VS Code**: Terminal → Run Task → Maven: package

**Запуск JAR:**
```bash
java -jar target/client-server-1.0.0.jar
```

## Проверка после запуска

После успешного запуска откройте в браузере:
- http://localhost:8080/api/products

Должен вернуться JSON с товарами.

## Получение подробной информации об ошибке

**В IDE:**
- Проверьте консоль IDE - там будут показаны все ошибки
- Проверьте вкладку "Problems" или "Issues" в IDE
- В IntelliJ IDEA: View → Tool Windows → Problems

**Если используете Maven командной строки:**
```bash
mvn clean install -X
```

Это покажет подробную информацию об ошибке.

