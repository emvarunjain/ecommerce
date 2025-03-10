package com.example.ProductService.controller;

import com.example.ProductService.dto.ProductDto;
import com.example.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ProductDto> getAllProducts(){
        return productService.getAllProducts();
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateProduct(@RequestBody ProductDto productDto) {
        productService.updateProduct(productDto);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteAllProducts() {
        productService.deleteAllProducts();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
