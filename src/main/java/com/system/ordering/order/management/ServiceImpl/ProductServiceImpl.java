package com.system.ordering.order.management.ServiceImpl;

import com.system.ordering.order.management.DTOs.OrderResponseDto;
import com.system.ordering.order.management.Entity.Product;
import com.system.ordering.order.management.Repository.ProductRepo;
import com.system.ordering.order.management.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepo.findById(id);
    }

    @Autowired
    private ProductRepo productRepo;

    @Override
    public Product addProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }
}
