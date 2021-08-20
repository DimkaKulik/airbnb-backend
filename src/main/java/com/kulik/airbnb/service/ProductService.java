package com.kulik.airbnb.service;

import com.kulik.airbnb.dao.dto.ProductDto;
import com.kulik.airbnb.dao.dto.UserDto;
import com.kulik.airbnb.dao.impl.ProductDao;
import com.kulik.airbnb.dao.impl.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final UserDao userDao;
    private final ProductDao productDao;

    public ProductService(UserDao userDao, ProductDao productDao) {
        this.userDao = userDao;
        this.productDao = productDao;
    }

    public ResponseEntity<?> getProductsPage(int limit, int offset) {
        if (limit > 100) {
            limit = 100;
        }

        List<ProductDto> allProducts = productDao.getPage(limit, offset);

        if (allProducts != null) {
            return ResponseEntity.ok(allProducts);
        } else {
            return new ResponseEntity<>("Something went wrong, cannot extract products from DB", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> getProductById(int id) {
        ProductDto productDto = productDao.getById(id);

        if (productDto != null) {
            return ResponseEntity.ok(productDto);
        } else {
            return new ResponseEntity<>("No user with such id", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> createProduct(ProductDto productDto) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto authenticatedUserDto = userDao.getByEmail(authenticatedUserEmail);

        productDto.setApproved(false);
        productDto.setUsersId(authenticatedUserDto.getId());

        int status = productDao.create(productDto);

        if (status > 0) {
            return ResponseEntity.ok(status);
        } else {
            return new ResponseEntity<>("Something went wrong, cannot add new product into db", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> updateProduct(ProductDto productDto) {
        productDto.setApproved(null);
        productDto.setUsersId(null);

        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String productUserEmail = productDao.getUserEmailByProductId(productDto.getId());

        if (authenticatedUserEmail.equals(productUserEmail)) {
            int status = productDao.update(productDto);
            return status == 0 ? new ResponseEntity<>("Cannot update product", HttpStatus.CONFLICT)
                    : ResponseEntity.ok(status);
        } else {
            return new ResponseEntity<>("No rights to update product", HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> deleteProduct(ProductDto productDto) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String productUserEmail = productDao.getUserEmailByProductId(productDto.getId());

        if (authenticatedUserEmail.equals(productUserEmail)) {
            int status = productDao.delete(productDto);
            return status == 0 ? new ResponseEntity<>("Cannot delete product", HttpStatus.CONFLICT)
                    : ResponseEntity.ok(status);
        } else {
            return new ResponseEntity<>("No rights to delete product", HttpStatus.FORBIDDEN);
        }
    }
}
