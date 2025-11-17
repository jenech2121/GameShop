# Быстрое решение: UnsupportedClassVersionError

## Проблема
```
UnsupportedClassVersionError: class file version 55.0
```

Проект собирается под **Java 11**. Если вы запускаете его на Java 8 (52.0) или ниже, появится эта ошибка.

## Что делать (5 минут)

### 1. Установите Java 11
1. Откройте https://adoptium.net/
2. Скачайте Temurin **JDK 11 (LTS)**
3. Установите (Windows/Mac/Linux)

### 2. Настройте IntelliJ IDEA
1. `Ctrl+Alt+Shift+S` → Project Structure
2. Project SDK → выберите установленный **Java 11**
3. Project language level → 11
4. OK
5. File → Settings → Build, Execution, Deployment → Compiler → Java Compiler → Project bytecode version = 11

### 3. Настройте конфигурацию запуска
1. Run → Edit Configurations
2. Выберите `GameShopApplication` (или Maven конфигурацию)
3. Поле **JRE** → Java 11
4. OK

### 4. Очистите и пересоберите
```
mvn clean install
```
или в IntelliJ: Build → Rebuild Project

### 5. Запустите снова
- `mvn spring-boot:run` или метод `main()` в `GameShopApplication`

## Проверка
```bash
java -version
```
Должно показать `11.0.x`.

Если видите другую версию — обновите переменную окружения `JAVA_HOME` и перезапустите терминал.

Готово ✅

