package com.kulik.airbnb.service;

import com.kulik.airbnb.model.Product;
import com.kulik.airbnb.model.ServiceResponse;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.dao.impl.ProductDao;
import com.kulik.airbnb.dao.impl.UserDao;
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

    public ServiceResponse<?> getProductsPage(int limit, int offset) {
        if (limit > 100) {
            limit = 100;
        }

        List<Product> allProducts = productDao.getPage(limit, offset);

        if (allProducts == null) {
            return new ServiceResponse<>("Cannot select all products", null);
        } else {
            return new ServiceResponse<>("ok", allProducts);
        }
    }

    public ServiceResponse<?> getProductById(int id) {
        Product product = productDao.getById(id);

        if (product == null) {
            return new ServiceResponse<>("Cannot create product", null);
        } else {
            return new ServiceResponse<>("ok", product);
        }
    }

    public ServiceResponse<?> createProduct(Product product) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User authenticatedUser = userDao.getByEmail(authenticatedUserEmail);

        product.setApproved(false);
        product.setUsersId(authenticatedUser.getId());

        int status = productDao.create(product);

        if (status == 0) {
            return new ServiceResponse<>("Cannot create product", null);
        } else {
            return new ServiceResponse<>("ok", status);
        }
    }

    public ServiceResponse<?> updateProduct(Product product) {
        product.setApproved(null);
        product.setUsersId(null);

        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String productUserEmail = productDao.getUserEmailByProductId(product.getId());

        if (authenticatedUserEmail.equals(productUserEmail)) {
            int status = productDao.update(product);
            return status == 0 ? new ServiceResponse<>("Cannot update product", null)
                    : new ServiceResponse<>("ok", status);
        } else {
            return new ServiceResponse<>("No rights to update product", null);
        }
    }

    public ServiceResponse<?> deleteProduct(Product product) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String productUserEmail = productDao.getUserEmailByProductId(product.getId());

        if (authenticatedUserEmail.equals(productUserEmail)) {
            int status = productDao.delete(product);
            return status == 0 ? new ServiceResponse<>("Cannot delete product", null)
                    : new ServiceResponse<>("ok", status);
        } else {
            return new ServiceResponse<>("No rights to delete product", null);
        }
    }
}
