package com.aptech.shop.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final String imagesDirectory = "static/images";

    @GetMapping("/{imageName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName){
        Path imagePath = Paths.get(imagesDirectory).resolve(imageName);
        System.out.println(imagePath);
        try {
            Resource resource = new UrlResource(imagePath.toUri());
            if(resource.exists() && resource.isReadable()){
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            }else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
