package com.example.OrderService.service;

import com.example.OrderService.dto.InventoryDto;
import com.example.OrderService.dto.OrderDto;
import com.example.OrderService.dto.OrderLineItemDto;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderLineItem;
import com.example.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder lBWebClientBuilder;

    public void placeOrder(OrderDto orderDto) {
        List<OrderLineItem> orderLineItems = orderDto.getLineItems().stream().map(this::maptoOrderLineItem).toList();
        List<String> skuCodes = orderLineItems.stream().map(OrderLineItem::getSkuCode).toList();
        Order order = Order.builder()
                .lineItems(orderLineItems)
                .build();
        InventoryDto[] inventoryDtos = lBWebClientBuilder.build().get().uri("http://inventoryservice/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryDto[].class)
                .block();
        boolean allInStock = false;
        if (inventoryDtos != null && inventoryDtos.length > 0) {
            Map<String, Integer> inventoryMap = Arrays.stream(inventoryDtos)
                    .collect(Collectors.toMap(InventoryDto::getSkuCode, InventoryDto::getQuantity));
            allInStock = orderLineItems.stream()
                    .allMatch(item -> inventoryMap.getOrDefault(item.getSkuCode(), 0) >= item.getQuantity());
        }
        if (!allInStock) {
            throw new RuntimeException("Inventory is not available for every SKU");
        }

        orderRepository.save(order);
        log.info("Order {} created successfully", order.getId());
    }

    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::maptoOrderDto).toList();
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
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
        orderRepository.save(order);
        log.info("Order {} updated successfully", order.getId());
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
        log.info("Order {} deleted successfully", id);
    }
}
