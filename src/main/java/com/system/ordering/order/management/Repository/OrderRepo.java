package com.system.ordering.order.management.Repository;

import com.system.ordering.order.management.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Integer> {

}

