package com.kulik.airbnb.service;

import com.kulik.airbnb.dao.impl.ProductDao;
import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.model.Product;
import com.kulik.airbnb.model.User;
import com.kulik.airbnb.security.JwtUserDetails;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class ProductServiceTest {

    private static final String EMAIL = "someemail@mail.com";

    @Mock
    private UserDao userDao;

    @Mock
    private ProductDao productDao;

    private ProductService productService;

    public ProductServiceTest() {
        MockitoAnnotations.initMocks(this);
        this.productService = new ProductService(userDao, productDao);
    }

    @Test
    void updateProductAuthenticatedUser() {
        given(productDao.getUserEmailByProductId(any())).willReturn(EMAIL);
        given(productDao.update(any(Product.class))).willReturn(1);

        Authentication authentication = Mockito.mock(Authentication.class);
        given(authentication.getName()).willReturn(EMAIL);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        given(securityContext.getAuthentication()).willReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        assertEquals(1, productService.updateProduct(new Product()));
    }

    @Test
    void updateProductUnauthenticatedUser() {
        given(productDao.getUserEmailByProductId(anyLong())).willReturn(EMAIL);

        Authentication authentication = Mockito.mock(Authentication.class);
        given(authentication.getName()).willReturn(EMAIL + "some text");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        given(securityContext.getAuthentication()).willReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        assertEquals(-1, productService.updateProduct(new Product()));
    }
}
