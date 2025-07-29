package com.system.ordering.order.management.Controller;

import com.system.ordering.order.management.DTOs.OrderRequestDto;
import com.system.ordering.order.management.DTOs.OrderResponseDto;
import com.system.ordering.order.management.Services.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //Create a new order
    @PostMapping
    public OrderResponseDto placeOrder(@Valid @RequestBody OrderRequestDto order) {
        return orderService.placeOrder(order);
    }

    // Get all orders
    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Get single order by ID
    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }
    // Update quantity of a product or product in an order
    //orders/1/product/3?quantity=4
    @PutMapping("/{orderId}/product/{productId}")
    public OrderResponseDto addOrUpdateProductInOrder(
            @PathVariable Integer orderId,
            @PathVariable Integer productId,
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1")Integer quantity) {

        return orderService.addOrUpdateProductInOrder(orderId, productId, quantity);
    }
    //delete product from order
    @DeleteMapping("/{orderId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromOrder(
            @PathVariable Integer orderId,
            @PathVariable Integer productId) {

        orderService.deleteProductFromOrder(orderId, productId);
        return ResponseEntity.ok("Product removed from order and stock updated successfully.");
    }

    // Delete entire order
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Integer orderId) {
        orderService.deleteOrder(orderId);
    }
}

