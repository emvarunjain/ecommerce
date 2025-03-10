package com.example.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class OrderLineItemDto {
    private Long id;
    private String skuCode;
    private int quantity;
    private double price;
}
