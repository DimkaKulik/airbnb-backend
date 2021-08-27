package com.kulik.airbnb.controller;

import com.kulik.airbnb.model.Product;
import com.kulik.airbnb.model.ServiceResponse;
import com.kulik.airbnb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    ResponseEntity<?> getProductsPage(@RequestParam(value = "limit", required = false, defaultValue = "100") int limit,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
        List<Product> productsPage = productService.getProductsPage(limit, offset);

        if (productsPage != null) {
            return ResponseEntity.ok(productsPage);
        } else {
            return new ResponseEntity<>("Cannot get products page", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);

        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return new ResponseEntity<>("Cannot get product", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping
    ResponseEntity<?> createProduct(@RequestBody Product product) {
        int status = productService.createProduct(product);

        if (status > 0) {
            return ResponseEntity.ok(status);
        } else {
            return new ResponseEntity<>("Cannot register user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping
    ResponseEntity<?> updateProduct(@RequestBody Product product) {
        int status = productService.updateProduct(product);

        if (status > 0) {
            return ResponseEntity.ok(status);
        } else if (status == 0) {
            return new ResponseEntity<>("Cannot update user", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>("You are not allowed to do this operation", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping
    ResponseEntity<?> deleteProduct(@RequestBody Product product) {
        int status = productService.deleteProduct(product);

        if (status > 0) {
            return ResponseEntity.ok(status);
        } else if (status == 0) {
            return new ResponseEntity<>("Cannot delete user", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>("You are not allowed to do this operation", HttpStatus.FORBIDDEN);
        }
    }
}
