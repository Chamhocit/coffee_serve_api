package com.aptech.shop.demo.repository;

import com.aptech.shop.demo.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
