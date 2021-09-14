package com.kulik.airbnb.service;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.kulik.airbnb.dao.impl.ProductDao;
import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    private final Storage storage;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Value("${avatar_bucket}")
    String avatarBucket;

    @Value("${product_photo_bucket}")
    String productPhotoBucket;

    public ImageService(Storage storage, UserDao userDao, ProductDao productDao) {
        this.storage = storage;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    public void updateAvatar(MultipartFile file) throws IOException {
        User authenticatedUser = userDao.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        String filename = authenticatedUser.getId().toString() + ".png";
        storage.create(
                BlobInfo.newBuilder(avatarBucket, filename).build(),
                file.getBytes()
        );

        authenticatedUser.setAvatar("/" + avatarBucket + "/" + filename);
        userDao.update(authenticatedUser);
    }

    MultipartFile cropImage(MultipartFile file) {
        return file;
    }

}
