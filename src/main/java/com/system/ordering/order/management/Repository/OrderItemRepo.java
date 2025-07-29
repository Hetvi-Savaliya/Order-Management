package com.system.ordering.order.management.Repository;

import com.system.ordering.order.management.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

    // Get all items in a specific order
    List<OrderItem> findByOrderId(Integer orderId);
    //check if thst order id is exist or not valid or not

    // Find one item in an order by orderId and productId
    Optional<OrderItem> findByOrderIdAndProductId(Integer orderId, Integer productId);

    // Delete a product from an order
    @Transactional
    void deleteByOrderIdAndProductId(Integer orderId, Integer productId);

    // Update quantity and subtotal of a product in an order
    @Transactional
    @Modifying
    @Query("UPDATE OrderItem oi SET oi.quantity = :quantity, oi.subtotal = :subtotal " +
            "WHERE oi.order.id = :orderId AND oi.product.id = :productId")
    void updateQuantityInOrder(Integer orderId, Integer productId, Integer quantity, BigDecimal subtotal);
}

