package com.system.ordering.order.management.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderResponseDto {
    @NotNull()
    private int orderId;

    private LocalDateTime orderTime;

    private BigDecimal totalAmount;

    @NotEmpty()
    private List<OrderItemDto> items;
}

