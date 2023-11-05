package com.aptech.shop.demo.controller;

import com.aptech.shop.demo.entity.Product;
import com.aptech.shop.demo.request.ProductRequest;
import com.aptech.shop.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/product")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest productRequest) throws IOException {
        Product product = productService.AddProduct(productRequest);
        return ResponseEntity.ok(product);
    }
}
