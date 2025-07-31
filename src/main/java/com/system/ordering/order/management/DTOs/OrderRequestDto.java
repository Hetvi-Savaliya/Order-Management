package com.system.ordering.order.management.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class OrderRequestDto {

    private List<@Valid ProductQuantity> products;

    @Setter
    @Getter
    @Valid
    public static class ProductQuantity {

        @NotNull()
        private int productId;

        @NotNull()
        @Min(value = 1)
        private Integer quantity;
    }
}

