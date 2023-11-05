package com.aptech.shop.demo.request;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductRequest {

    private String name;

    private String description;

    private int quantity;

    private String category;

    private MultipartFile img_one;

    private MultipartFile img_two;

    private MultipartFile img_three;

}
