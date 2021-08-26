package com.kulik.airbnb.controller;

import com.kulik.airbnb.model.Product;
import com.kulik.airbnb.model.ServiceResponse;
import com.kulik.airbnb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        ServiceResponse response = productService.getProductsPage(limit, offset);

        return ServiceResponse.returnResponseEntity(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getProductById(@PathVariable int id) {
        ServiceResponse response = productService.getProductById(id);

        return ServiceResponse.returnResponseEntity(response);
    }

    @PostMapping("/")
    ResponseEntity<?> createProduct(@RequestBody Product product) {
        ServiceResponse response = productService.createProduct(product);

        return ServiceResponse.returnResponseEntity(response);
    }

    @PatchMapping("/")
    ResponseEntity<?> updateProduct(@RequestBody Product product) {
        ServiceResponse response = productService.updateProduct(product);

        return ServiceResponse.returnResponseEntity(response);
    }

    @DeleteMapping("/")
    ResponseEntity<?> deleteProduct(@RequestBody Product product) {
        ServiceResponse response = productService.deleteProduct(product);

        return ServiceResponse.returnResponseEntity(response);
    }
}
