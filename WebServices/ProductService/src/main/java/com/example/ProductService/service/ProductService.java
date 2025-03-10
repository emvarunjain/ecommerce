package com.example.ProductService.service;

import com.example.ProductService.dto.ProductDto;
import com.example.ProductService.dto.ProductDto;
import com.example.ProductService.model.Product;
import com.example.ProductService.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product {} created successfully", product.getId());
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::maptoProductDto).toList();
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    private ProductDto maptoProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public void updateProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product {} updated successfully", product.getId());
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        log.info("Product {} deleted successfully", id);
    }
}
