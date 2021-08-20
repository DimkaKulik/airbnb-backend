package com.kulik.airbnb.controller;

import com.kulik.airbnb.dao.dto.ProductDto;
import com.kulik.airbnb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    ResponseEntity<?> getProductsPage(@RequestParam(value = "limit", required = false, defaultValue = "100") int limit,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        return productService.getProductsPage(limit, offset);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping("/")
    ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PatchMapping("/")
    ResponseEntity<?> updateProduct(@RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @DeleteMapping("/")
    ResponseEntity<?> deleteProduct(@RequestBody ProductDto productDto) {
        return productService.deleteProduct(productDto);
    }
}
