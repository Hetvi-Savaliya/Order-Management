package com.system.ordering.order.management.Controller;

import com.system.ordering.order.management.DTOs.OrderRequestDto;
import com.system.ordering.order.management.DTOs.OrderResponseDto;
import com.system.ordering.order.management.Services.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderResponseDto placeOrder(@Valid @RequestBody OrderRequestDto order) {
        System.out.println();
        return orderService.placeOrder(order);
    }

    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{orderId}/product/{productId}")
    public OrderResponseDto addOrUpdateProductInOrder(
            @PathVariable int orderId,
            @PathVariable int productId,
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1")int quantity) {
        return orderService.addOrUpdateProductInOrder(orderId, productId, quantity);
    }

    @DeleteMapping("/{orderId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromOrder(
            @PathVariable int orderId,
            @PathVariable int productId) {
        orderService.deleteProductFromOrder(orderId, productId);
        return ResponseEntity.ok("Product removed from order and stock updated successfully.");
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable int orderId) {
        orderService.deleteOrder(orderId);
    }
}

