package com.example.InventoryService.service;

import com.example.InventoryService.dto.InventoryDto;
import com.example.InventoryService.model.Inventory;
import com.example.InventoryService.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }

    public void addInventory(InventoryDto inventoryDto) {
        Inventory inventory = Inventory.builder()
                .skuCode(inventoryDto.getSkuCode())
                .quantity(inventoryDto.getQuantity())
                .id(inventoryDto.getId())
                .build();
        inventoryRepository.save(inventory);
        log.info("Inventory {} created successfully", inventory.getId());
    }

    public List<InventoryDto> getInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories.stream().map(this::maptoInventoryDto).toList();
    }

    private InventoryDto maptoInventoryDto(Inventory inventory) {
        return InventoryDto.builder()
                .id(inventory.getId())
                .skuCode(inventory.getSkuCode())
                .quantity(inventory.getQuantity())
                .build();
    }
}
