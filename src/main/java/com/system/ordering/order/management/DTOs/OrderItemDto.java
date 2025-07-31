package com.system.ordering.order.management.DTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
public class OrderItemDto {

    @NotNull()
    private int productId;

    @NotBlank
    @Column(length = 50,nullable = false)
    private String productName;

    @NotNull()
    @Min(value = 1)
    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subtotal;
}

