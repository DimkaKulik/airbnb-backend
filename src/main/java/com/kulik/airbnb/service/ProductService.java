package com.kulik.airbnb.service;

import com.kulik.airbnb.model.Product;
import com.kulik.airbnb.dao.impl.ProductDao;
import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final UserDao userDao;
    private final ProductDao productDao;

    @Value("${google_cloud_url}")
    String googleCloudUrl;

    public ProductService(UserDao userDao, ProductDao productDao) {
        this.userDao = userDao;
        this.productDao = productDao;
    }

    public List<Product> getProductsPage(int limit, int offset) {
        if (limit > 100) {
            limit = 100;
        }

        List<Product> allProducts = productDao.getPage(limit, offset);
        for (Product product : allProducts) {
            if (product.getMainPhoto() != null) {
                product.setMainPhoto(googleCloudUrl + product.getMainPhoto());
            }
        }
        return allProducts;
    }

    public Product getProductById(Long id) {
        Product product = productDao.getById(id);
        if (product.getMainPhoto() != null) {
            product.setMainPhoto(googleCloudUrl + product.getMainPhoto());
        }
        return product;
    }

    public int createProduct(Product product) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User authenticatedUser = userDao.getByEmail(authenticatedUserEmail);

        product.setApproved(false);
        product.setUsersId(authenticatedUser.getId());

        int status = productDao.create(product);

        return status;
    }

    public int updateProduct(Product product) {
        product.setApproved(null);
        product.setUsersId(null);

        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String productUserEmail = productDao.getUserEmailByProductId(product.getId());

        if (authenticatedUserEmail.equals(productUserEmail)) {
            int status = productDao.update(product);

            return status;
        } else {
            return -1;
        }
    }

    public int deleteProduct(Product product) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String productUserEmail = productDao.getUserEmailByProductId(product.getId());

        if (authenticatedUserEmail.equals(productUserEmail)) {
            int status = productDao.delete(product);
            return status;
        } else {
            return -1;
        }
    }
}
