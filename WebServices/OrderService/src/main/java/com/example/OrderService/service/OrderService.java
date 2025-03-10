package com.example.OrderService.service;

import com.example.OrderService.dto.OrderDto;
import com.example.OrderService.dto.OrderLineItemDto;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderLineItem;
import com.example.OrderService.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository OrderRepository;

    public void placeOrder(OrderDto orderDto) {
        List<OrderLineItem> orderLineItems = orderDto.getLineItems().stream().map(this::maptoOrderLineItem).toList();
        Order order = Order.builder()
                .lineItems(orderLineItems)
                .build();
        OrderRepository.save(order);
        log.info("Order {} created successfully", order.getId());
    }

    public List<OrderDto> getAllOrders() {
        List<Order> orders = OrderRepository.findAll();
        return orders.stream().map(this::maptoOrderDto).toList();
    }

    public void deleteAllOrders() {
        OrderRepository.deleteAll();
    }

    private OrderDto maptoOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .build();
    }

    private OrderLineItem maptoOrderLineItem(OrderLineItemDto orderLineItemDto) {
        return OrderLineItem.builder()
                .id(orderLineItemDto.getId())
                .skuCode(orderLineItemDto.getSkuCode())
                .quantity(orderLineItemDto.getQuantity())
                .price(orderLineItemDto.getPrice())
                .build();
    }

    public void updateOrder(OrderDto orderDto) {
        List<OrderLineItem> orderLineItems = orderDto.getLineItems().stream().map(this::maptoOrderLineItem).toList();
        Order order = Order.builder()
                .lineItems(orderLineItems)
                .build();
        OrderRepository.save(order);
        log.info("Order {} updated successfully", order.getId());
    }

    public void deleteOrder(Long id) {
        OrderRepository.deleteById(id);
        log.info("Order {} deleted successfully", id);
    }
}
