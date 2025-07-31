package com.system.ordering.order.management.Services;

import com.system.ordering.order.management.DTOs.OrderRequestDto;
import com.system.ordering.order.management.DTOs.OrderResponseDto;
import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto order);

    OrderResponseDto getOrderById(int orderId);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto addOrUpdateProductInOrder(int orderId, int productId, Integer quantity);

    void deleteProductFromOrder(int orderId, int productId);

    void deleteOrder(int orderId);
}
