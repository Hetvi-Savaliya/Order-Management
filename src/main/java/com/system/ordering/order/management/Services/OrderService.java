package com.system.ordering.order.management.Services;

import com.system.ordering.order.management.DTOs.OrderRequestDto;
import com.system.ordering.order.management.DTOs.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto placeOrder(OrderRequestDto order);

    OrderResponseDto getOrderById(Integer orderId);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto addOrUpdateProductInOrder(Integer orderId, Integer productId, Integer quantity);

    void deleteProductFromOrder(Integer orderId, Integer productId);

    void deleteOrder(Integer orderId);
}
