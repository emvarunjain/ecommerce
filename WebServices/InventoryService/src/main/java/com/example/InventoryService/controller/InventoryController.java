package com.example.InventoryService.controller;

import com.example.InventoryService.dto.InventoryDto;
import com.example.InventoryService.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public String addInventory(@RequestBody InventoryDto inventoryDto) {
        inventoryService.addInventory(inventoryDto);
        return "Inventory placed successfully";
    }

    @GetMapping("/{skuCode}")
    @ResponseStatus(code = HttpStatus.OK)
    public boolean isInStock(@PathVariable String skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<InventoryDto> getInventory() {
        return inventoryService.getInventory();
    }
}
