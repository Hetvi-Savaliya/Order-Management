package com.system.ordering.order.management.ServiceImpl;

import com.system.ordering.order.management.DTOs.OrderItemDto;
import com.system.ordering.order.management.DTOs.OrderRequestDto;
import com.system.ordering.order.management.DTOs.OrderResponseDto;
import com.system.ordering.order.management.Entity.Order;
import com.system.ordering.order.management.Entity.OrderItem;
import com.system.ordering.order.management.Entity.Product;
import com.system.ordering.order.management.Exception.OrderNotFoundException;
import com.system.ordering.order.management.Exception.ProductNotFoundException;
import com.system.ordering.order.management.Exception.StockInsufficientException;
import com.system.ordering.order.management.Repository.OrderItemRepo;
import com.system.ordering.order.management.Repository.OrderRepo;
import com.system.ordering.order.management.Repository.ProductRepo;
import com.system.ordering.order.management.Services.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    // Place new order
    @Override
    public OrderResponseDto placeOrder(OrderRequestDto request) {
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        Order order = new Order();

        for (OrderRequestDto.ProductQuantity pq : request.getProducts()) {
            System.out.println("product....." + pq.getProductId() +" "+ pq.getQuantity());
            Product product = productRepo.findById(pq.getProductId())
              .orElseThrow(() -> new ProductNotFoundException("Product not found: " + pq.getProductId()));
            if (product.getStock() < pq.getQuantity()) {
                throw new StockInsufficientException("Only " + product.getStock() + " units available for product: " + product.getName());
            }
            product.setStock(product.getStock() - pq.getQuantity());
            productRepo.save(product);

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(pq.getQuantity()));
            total = total.add(subtotal);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(pq.getQuantity());
            item.setSubtotal(subtotal);
            orderItems.add(item);
        }
        order.setItems(orderItems);
        order.setTotalAmount(total);
        Order savedOrder = orderRepo.save(order);
        return mapToResponseDto(savedOrder);
    }

    //Get one order
    @Override
    public OrderResponseDto getOrderById(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        return mapToResponseDto(order);
    }

    //Get all orders
    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto addOrUpdateProductInOrder(Integer orderId, Integer productId, Integer quantity) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
        }
        product.setStock(product.getStock() - quantity);
        productRepo.save(product);

        // Check if the item already exists in the order
        OrderItem item = orderItemRepo.findByOrderIdAndProductId(orderId, productId)
                .orElse(null);

        if (item == null) {
            // Create new item
            item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(quantity);
        } else {
            // Update quantity only
            item.setQuantity(quantity);
        }

        // Calculate subtotal and save
        item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        orderItemRepo.save(item);

        // Recalculate total order amount
        BigDecimal total = orderItemRepo.findByOrderId(orderId).stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        orderRepo.save(order);

        return mapToResponseDto(order);
    }
    //delete the product from order
    @Transactional
    @Override
    public void deleteProductFromOrder(Integer orderId, Integer productId) {
        // 1. Fetch OrderItem
        OrderItem item = orderItemRepo.findByOrderIdAndProductId(orderId, productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found in the order."));

        // 2. Restore stock to product
        Product product = item.getProduct();
        int restoredQty = item.getQuantity();
        product.setStock(product.getStock() + restoredQty);
        productRepo.save(product);

        // 3. Delete OrderItem
        orderItemRepo.delete(item); // safer than custom delete

        // 4. Recalculate total
        List<OrderItem> items = orderItemRepo.findByOrderId(orderId);
        BigDecimal newTotal = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5. Update order
        Order order = item.getOrder();
        order.setTotalAmount(newTotal);
        order.setItems(items);
        orderRepo.save(order);
    }

    // delete the entire order
    @Transactional
    @Override
    public void deleteOrder(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        // Restore stock for each product in the order
        List<OrderItem> items = orderItemRepo.findByOrderId(orderId);
        for (OrderItem item : order.getItems()) {
            Product product = productRepo.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product ID " + item.getProduct().getId() + " not found"));
            product.setStock(product.getStock() + item.getQuantity());
            productRepo.save(product);
        }
        // Delete all items
        orderItemRepo.deleteAll(items);
        // Delete the order
        orderRepo.delete(order);
    }

    //Utility method to map Order → OrderResponseDto
    private OrderResponseDto mapToResponseDto(Order order) {
        OrderResponseDto response = new OrderResponseDto();
        response.setOrderId(order.getId());
        response.setOrderTime(order.getOrderTime());
        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemDto> itemDtos = order.getItems().stream().map(item -> {
            OrderItemDto dto = new OrderItemDto();
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getProduct().getPrice());
            dto.setSubtotal(item.getSubtotal());
            return dto;
        }).collect(Collectors.toList());

        response.setItems(itemDtos);
        return response;
    }
}

