package com.system.ordering.order.management.Services;

import com.system.ordering.order.management.Entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product addProduct(Product product);

    List<Product> getAllProducts();

    Optional<Product> getProductById(int productId);
}
