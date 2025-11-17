# Решение ошибки: UnsupportedClassVersionError

## Проблема
```
java.lang.UnsupportedClassVersionError: com/gameshop/GameShopApplication
has been compiled by a more recent version of the Java Runtime (class file version 55.0),
this version of the Java Runtime only recognizes class file versions up to 52.0
```

**Причина:** Проект компилируется под Java 11 (класс-файлы версии 55.0). Если вы запускаете его на Java 8 (52.0) или более старой версии, появится эта ошибка.

## Решение

### 1. Установите Java 11 (LTS)
1. Перейдите на https://adoptium.net/
2. Выберите **Temurin 11 (LTS)**
3. Скачайте и установите JDK

### 2. Настройте IntelliJ IDEA на Java 11

**Project SDK**
- File → Project Structure (Ctrl+Alt+Shift+S)
- Project → Project SDK: выберите установленную Java 11
- Project → Project language level: 11
- OK

**Compiler**
- File → Settings (Ctrl+Alt+S)
- Build, Execution, Deployment → Compiler → Java Compiler
- Project bytecode version: 11
- OK

**Run Configuration**
- Run → Edit Configurations
- Найдите `GameShopApplication` (или Maven конфигурацию)
- JRE: выберите Java 11
- OK

### 3. Проверка в терминале
```bash
java -version
```
Должно быть не ниже `11.0.x`.

### 4. После установки
1. File → Invalidate Caches / Restart → Invalidate and Restart
2. Build → Rebuild Project
3. Запустите заново (`spring-boot:run` или main-метод)

## Советы
- Если у вас уже установлен JDK 17+, можно использовать его — проект тоже будет работать.
- Главное, чтобы версия JDK, выбранная в IntelliJ и в Maven конфигурации, совпадала и была ≥ 11.

