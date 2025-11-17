package com.gameshop.repository;

import com.gameshop.model.Order;
import com.gameshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findAllByOrderByCreatedAtDesc();
    List<Order> findByDeliveryStatusOrderByCreatedAtDesc(Order.DeliveryStatus status);
    List<Order> findByDeliveryStatusAndExpectedDeliveryAtBefore(Order.DeliveryStatus status, java.time.LocalDateTime dateTime);
}




