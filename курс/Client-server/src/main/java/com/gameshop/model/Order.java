package com.gameshop.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @Column(nullable = false)
    private Double totalPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus = DeliveryStatus.IN_PROGRESS;

    @Column(name = "expected_delivery_at")
    private LocalDateTime expectedDeliveryAt;
    
    @Column(length = 50)
    private String paymentMethod;
    
    @Column(length = 50)
    private String paymentType;
    
    @Column(length = 255)
    private String deliveryAddress;
    
    @Column(length = 20)
    private String cardNumber;
    
    @Column(length = 5)
    private String cardExpiry;
    
    @Column(length = 3)
    private String cardCvv;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public enum OrderStatus {
        PENDING,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }

    public enum DeliveryStatus {
        IN_PROGRESS,
        AWAITING_CONFIRMATION,
        DELIVERED,
        CANCELLED
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}




