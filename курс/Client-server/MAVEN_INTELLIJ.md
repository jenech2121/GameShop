# Запуск через Maven в IntelliJ IDEA

## Быстрый способ (3 шага)

### Шаг 1: Откройте Maven панель
- В правой части IntelliJ IDEA найдите вкладку **Maven**
- Если её нет: View → Tool Windows → Maven (или Alt+1, затем выберите Maven)

### Шаг 2: Найдите spring-boot:run
- Разверните ваш проект в дереве Maven
- Разверните **Plugins** → **spring-boot**
- Найдите **spring-boot:run**

### Шаг 3: Запустите
- Дважды кликните на **spring-boot:run**
- Или правой кнопкой → Run 'spring-boot:run'

Готово! Сервер запустится в консоли IntelliJ IDEA.

---

## Способ 2: Через Run Configuration (для постоянного использования)

### Создание конфигурации:

1. **Run → Edit Configurations** (или Alt+Shift+F10, затем Edit Configurations)

2. **Нажмите "+"** в левом верхнем углу → выберите **Maven**

3. **Заполните поля:**
   - **Name:** `Spring Boot Run` (или любое удобное имя)
   - **Working directory:** 
     - Нажмите на папку справа
     - Выберите папку `Client-server` в вашем проекте
     - Или введите: `$PROJECT_DIR$/курс/Client-server`
   - **Command line:** `spring-boot:run`
   - **JRE:** выберите Java 11

4. **Нажмите OK**

5. **Запустите:**
   - Выберите конфигурацию "Spring Boot Run" в выпадающем списке Run
   - Нажмите Run (зеленая стрелка) или Shift+F10

---

## Способ 3: Через терминал IntelliJ IDEA

1. Откройте встроенный терминал: View → Tool Windows → Terminal (или Alt+F12)

2. Перейдите в папку проекта:
   ```bash
   cd курс/Client-server
   ```

3. Запустите:
   ```bash
   mvn spring-boot:run
   ```

---

## Другие полезные Maven команды

### Очистка и пересборка:
В Maven панели:
- **clean** - очистка скомпилированных файлов
- **install** - компиляция и установка в локальный репозиторий
- **package** - создание JAR файла

Или в терминале:
```bash
mvn clean install
mvn package
```

### Запуск с параметрами:
В Run Configuration можно добавить параметры в поле "Command line":
- `spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"`
- `spring-boot:run -X` (подробный вывод)

---

## Проверка работы

После запуска в консоли вы увидите:
```
Started GameShopApplication in X.XXX seconds
```

Откройте в браузере:
- http://localhost:8080/api/products

---

## Решение проблем

### Maven панель не видна
- View → Tool Windows → Maven
- Или Alt+1, затем выберите Maven

### Ошибка "Maven not found"
- IntelliJ IDEA использует встроенный Maven
- File → Settings → Build, Execution, Deployment → Build Tools → Maven
- Убедитесь, что выбран "Bundled (Maven 3)" или укажите путь к Maven

### Ошибка Java версии
- Убедитесь, что установлена Java 11 (или выше)
- File → Project Structure → Project SDK: Java 11
- См. файл `FIX_JAVA_VERSION.md` для подробностей

### Зависимости не загружаются
- Правой кнопкой на `pom.xml` → Maven → Reload Project
- Или в Maven панели: нажмите кнопку обновления (круговая стрелка)

---

## Преимущества запуска через Maven

✅ Автоматическая загрузка зависимостей  
✅ Правильная настройка classpath  
✅ Поддержка Spring Boot DevTools (автоперезагрузка)  
✅ Легко добавить параметры запуска  
✅ Единый способ для всех разработчиков  


