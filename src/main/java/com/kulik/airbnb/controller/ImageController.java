package com.kulik.airbnb.controller;

import com.kulik.airbnb.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/avatar")
    ResponseEntity<?> updateAvatar(@RequestParam("image") MultipartFile file) throws IOException {
        try {
            imageService.updateAvatar(file);
            return ResponseEntity.ok("Avatar was updated");
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot update avatar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
