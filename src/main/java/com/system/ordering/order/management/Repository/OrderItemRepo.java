package com.system.ordering.order.management.Repository;

import com.system.ordering.order.management.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderId(int orderId);

    Optional<OrderItem> findByOrderIdAndProductId(int orderId, int productId);
}

