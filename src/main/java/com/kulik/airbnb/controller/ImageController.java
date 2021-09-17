package com.kulik.airbnb.controller;

import com.kulik.airbnb.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/product/{id}")
    ResponseEntity<?> getProductPhoto(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(imageService.getPhotos(id));
        } catch (Exception e) {
            // + e.getMessage() added for development purposes
            return new ResponseEntity<>("Cannot get images: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/avatar")
    ResponseEntity<?> updateAvatar(@RequestParam("image") MultipartFile file) {
        try {
            imageService.updateAvatar(file);
            return ResponseEntity.ok("Avatar was updated");
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot update avatar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/product/{id}")
    ResponseEntity<?> uploadProductPhoto(@RequestParam("image") MultipartFile file, @PathVariable("id") Long id) {
        try {
            imageService.uploadPhoto(file, id);
            return ResponseEntity.ok("photo was uploaded");
        } catch (Exception e) {
            // + e.getMessage() added for development purposes
            return new ResponseEntity<>("Cannot upload image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
