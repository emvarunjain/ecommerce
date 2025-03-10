package com.example.OrderService.controller;

import com.example.OrderService.dto.OrderDto;
import com.example.OrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService OrderService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderDto orderDto) {
        OrderService.placeOrder(orderDto);
        return "Order placed successfully";
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<OrderDto> getAllOrders(){
        return OrderService.getAllOrders();
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateOrder(@RequestBody OrderDto orderDto) {
        OrderService.updateOrder(orderDto);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteAllOrders() {
        OrderService.deleteAllOrders();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteOrder(@PathVariable Long id) {
        OrderService.deleteOrder(id);
    }
}
