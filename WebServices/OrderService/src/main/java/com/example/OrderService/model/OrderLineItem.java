package com.example.OrderService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_line_item")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String skuCode;
    private int quantity;
    private double price;
}
