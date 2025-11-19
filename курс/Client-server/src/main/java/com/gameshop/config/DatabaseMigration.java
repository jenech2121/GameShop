package com.gameshop.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Миграция для добавления столбца full_name в таблицу users
 * Выполняется при старте приложения, если столбец отсутствует
 */
@Component
@Order(1) // Выполняется до DataInitializer
@RequiredArgsConstructor
@Slf4j
public class DatabaseMigration implements CommandLineRunner {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Override
    public void run(String... args) {
        try {
            // Проверяем, существует ли таблица users
            if (!tableExists("users")) {
                log.debug("Таблица users еще не создана. Hibernate создаст ее с правильной схемой.");
                return;
            }
            
            // Проверяем, существует ли столбец full_name
            if (!columnExists("users", "full_name")) {
                log.info("Столбец full_name не найден. Добавляем столбец в таблицу users...");
                
                // Добавляем столбец
                jdbcTemplate.execute(
                    "ALTER TABLE users ADD COLUMN full_name VARCHAR(255) NOT NULL DEFAULT 'User'"
                );
                
                log.info("Столбец full_name успешно добавлен в таблицу users");
                
                // Обновляем существующие записи, у которых full_name = 'User'
                // Устанавливаем имя на основе email (для H2 используем POSITION вместо LOCATE)
                try {
                    jdbcTemplate.update(
                        "UPDATE users SET full_name = SUBSTRING(email, 1, CASE WHEN POSITION('@' IN email) > 0 THEN POSITION('@' IN email) - 1 ELSE LENGTH(email) END) " +
                        "WHERE full_name = 'User' AND email IS NOT NULL AND email LIKE '%@%'"
                    );
                    log.info("Обновлены существующие записи пользователей");
                } catch (Exception e) {
                    log.warn("Не удалось обновить существующие записи: {}", e.getMessage());
                }
            } else {
                log.debug("Столбец full_name уже существует в таблице users");
            }
        } catch (Exception e) {
            log.error("Ошибка при выполнении миграции базы данных: {}", e.getMessage(), e);
            // Не прерываем запуск приложения, но логируем ошибку
        }
    }
    
    /**
     * Проверяет существование таблицы
     */
    private boolean tableExists(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_NAME = ?";
            
            Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                tableName.toUpperCase()
            );
            
            return count != null && count > 0;
        } catch (Exception e) {
            log.warn("Не удалось проверить существование таблицы {}: {}", 
                    tableName, e.getMessage());
            return false;
        }
    }
    
    /**
     * Проверяет существование столбца в таблице
     */
    private boolean columnExists(String tableName, String columnName) {
        try {
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE TABLE_NAME = ? AND COLUMN_NAME = ?";
            
            Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                tableName.toUpperCase(),
                columnName.toUpperCase()
            );
            
            return count != null && count > 0;
        } catch (Exception e) {
            log.warn("Не удалось проверить существование столбца {} в таблице {}: {}", 
                    columnName, tableName, e.getMessage());
            return false;
        }
    }
}

