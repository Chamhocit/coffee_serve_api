package com.aptech.shop.demo.controller;

import com.aptech.shop.demo.entity.Product;
import com.aptech.shop.demo.request.ProductRequest;
import com.aptech.shop.demo.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/all/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getPageProduct(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        Page<Product> productPage = productService.getPage(pageNumber, pageSize);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/search/findByName")
    public Page<Product> findByNameContaining(@RequestParam("name") String name, Pageable pageable){
        Page<Product> productPage = productService.getPageByName(name, pageable);
        return productPage;
    }

    @PostMapping(value = "/add" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@RequestPart("name") String name,
                                        @RequestPart("description") String description,
                                        @RequestPart("quantity") String quantity,
                                        @RequestPart("category") String category,
                                        @RequestPart("price") String price,
                                        @RequestPart("imgOne") MultipartFile imgOne,
                                        @RequestPart("imgTwo") MultipartFile imgTwo,
                                        @RequestPart("imgThree") MultipartFile imgThree) throws IOException {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(name);
        productRequest.setDescription(description);
        productRequest.setCategory(category);
        productRequest.setQuantity(Integer.valueOf(quantity));
        productRequest.setProPrice(Double.valueOf(price));
        productRequest.setImg_one(imgOne);
        productRequest.setImg_two(imgTwo);
        productRequest.setImg_three(imgThree);
        System.out.println(productRequest);
        Product product = productService.AddProduct(productRequest);
        return ResponseEntity.ok(product);
    }
}
