package com.aptech.shop.demo.service;

import com.aptech.shop.demo.entity.Product;
import com.aptech.shop.demo.entity.ProductImage;
import com.aptech.shop.demo.repository.ProductRepository;
import com.aptech.shop.demo.request.ProductRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
@Transactional
public class ProductService {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product AddProduct(ProductRequest productRequest) throws IOException {
        Product product = new Product();
        ProductImage productImage = new ProductImage();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setQuantity(productRequest.getQuantity());
        product.setDate(new Date());

        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        if(!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))){
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path fileOne = CURRENT_FOLDER.resolve(staticPath)
                        .resolve(imagePath).resolve(productRequest.getImg_one().getOriginalFilename());
        Path fileTwo = CURRENT_FOLDER.resolve(staticPath)
                        .resolve(imagePath).resolve(productRequest.getImg_two().getOriginalFilename());
        Path fileThree = CURRENT_FOLDER.resolve(staticPath)
                        .resolve(imagePath).resolve(productRequest.getImg_three().getOriginalFilename());
        try(OutputStream os = Files.newOutputStream(fileOne)){
            os.write(productRequest.getImg_one().getBytes());
        }
        try (OutputStream os = Files.newOutputStream(fileTwo)){
            os.write(productRequest.getImg_two().getBytes());
        }
        try (OutputStream os = Files.newOutputStream(fileThree)){
            os.write(productRequest.getImg_three().getBytes());
        }
        productImage.setImageOne(imagePath.resolve(productRequest.getImg_one().getOriginalFilename()).toString());
        productImage.setImageOne(imagePath.resolve(productRequest.getImg_two().getOriginalFilename()).toString());
        productImage.setImageOne(imagePath.resolve(productRequest.getImg_three().getOriginalFilename()).toString());
        product.setImg(productImage);
        Product productSave=productRepository.save(product);
        return productSave;
    }

}
