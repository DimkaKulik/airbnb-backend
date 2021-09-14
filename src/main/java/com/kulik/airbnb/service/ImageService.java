package com.kulik.airbnb.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.kulik.airbnb.dao.impl.ProductDao;
import com.kulik.airbnb.dao.impl.ProductPhotoDao;
import com.kulik.airbnb.dao.impl.UserDao;
import com.kulik.airbnb.model.Product;
import com.kulik.airbnb.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private final Storage storage;
    private final UserDao userDao;
    private final ProductDao productDao;
    private final ProductPhotoDao productPhotoDao;

    @Value("${avatar_bucket}")
    String avatarBucket;

    @Value("${product_photo_bucket}")
    String productPhotoBucket;

    @Value("${max_photo_allowed}")
    Long maxPhotoAllowed;

    @Value("${google_cloud_url}")
    String googleCloudUrl;

    public ImageService(Storage storage, UserDao userDao, ProductDao productDao, ProductPhotoDao productPhotoDao) {
        this.storage = storage;
        this.userDao = userDao;
        this.productDao = productDao;
        this.productPhotoDao = productPhotoDao;
    }

    public void updateAvatar(MultipartFile file) throws IOException {
        User authenticatedUser = userDao.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        String filename = authenticatedUser.getId() + ".png";
        storage.create(
                BlobInfo.newBuilder(avatarBucket, filename).build(),
                file.getBytes()
        );
        authenticatedUser.setAvatar("/" + avatarBucket + "/" + filename);
        userDao.update(authenticatedUser);
    }

    public void uploadPhoto(MultipartFile file, Long id) throws Exception {
        User productHost = productDao.getUserByProductId(id);
        if (productHost.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())
                && productPhotoDao.getPhotosByProductId(id) != null
                && productPhotoDao.getPhotosByProductId(id).size() <= maxPhotoAllowed) {
            String filename = id + "_" + UUID.randomUUID() + ".png";
            storage.create(
                    BlobInfo.newBuilder(productPhotoBucket, filename).build(),
                    file.getBytes()
            );

            Product product = productDao.getById(id);
            product.setMainPhoto("/" + productPhotoBucket + "/" + filename);
            productDao.update(product);

            productPhotoDao.createPhoto(id, productHost.getId(), "/" + productPhotoBucket + "/" + filename);
        } else {
            throw new Exception("Uploading photo to product you haven't created or " +
                    "uploading more than 10 photos not allowed");
        }
    }

    MultipartFile cropImage(MultipartFile file) {
        return file;
    }

    public List<String> getPhotos(Long id) {
        List<String> rawPhotosUrls = productPhotoDao.getPhotosByProductId(id);
        List<String> photoUrls = new ArrayList<>();
        for (String rawPhotoUrl : rawPhotosUrls) {
            photoUrls.add(googleCloudUrl + rawPhotoUrl);
        }
        return photoUrls;
    }
}
