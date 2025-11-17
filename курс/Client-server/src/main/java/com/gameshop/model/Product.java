package com.gameshop.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(length = 1000)
    private String image;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;
    
    @Column(length = 2000)
    private String description;
    
    @Column(nullable = false)
    private Integer stock = 0;
    
    public enum ProductCategory {
        GPU,              // Видеокарта
        MOTHERBOARD,      // Материнская Плата
        RAM,              // Оперативная память
        CPU,              // Процессор
        POWER,            // Блок питания
        SSD               // SSD накопитель
    }
}


