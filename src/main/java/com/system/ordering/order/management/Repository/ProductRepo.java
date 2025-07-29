package com.system.ordering.order.management.Repository;

import com.system.ordering.order.management.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {

}
