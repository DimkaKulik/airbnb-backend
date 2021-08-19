package com.kulik.airbnb.controller;

import com.kulik.airbnb.dao.dto.ProductDto;
import com.kulik.airbnb.dao.dto.UserDto;
import com.kulik.airbnb.dao.impl.ProductDao;
import com.kulik.airbnb.dao.impl.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProductController {

    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public ProductController(ProductDao productDao, UserDao userDao) {
        this.productDao = productDao;
        this.userDao = userDao;
    }

    //request param
    @GetMapping("/products")
    List<ProductDto> getAllProducts() {
        return productDao.getAll();
    }

    @GetMapping("/products/{id}")
    ProductDto getProductById(@PathVariable Long id) {
        return productDao.get(id);
    }

    @PostMapping("/products")
    Long createProduct(@RequestBody ProductDto productDto) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto authenticatedUserDto = userDao.get(authenticatedUserEmail);

        productDto.setApproved(false);
        productDto.setUsersId(authenticatedUserDto.getId());

        return productDao.create(productDto);
    }

    @PatchMapping("/products")
    int updateProduct(@RequestBody ProductDto productDto) {
        productDto.setApproved(null);
        productDto.setUsersId(null);

        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String productUserEmail = productDao.getUserEmailByProductId(productDto.getId());

        if (authenticatedUserEmail.equals(productUserEmail)) {
            return productDao.update(productDto);
        } else {
            return 0;
        }
    }

    @DeleteMapping("/products")
    int deleteProduct(@RequestBody ProductDto productDto) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String productUserEmail = productDao.getUserEmailByProductId(productDto.getId());

        if (authenticatedUserEmail.equals(productUserEmail)) {
            return productDao.delete(productDto);
        } else {
            return 0;
        }
    }
}
