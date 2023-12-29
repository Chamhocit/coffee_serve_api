package com.aptech.shop.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_image")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "image_one")
    private String imageOne;
    @Column(name = "image_two")
    private String imageTwo;
    @Column(name = "image_three")
    private String imageThree;
    @OneToOne
    @JoinColumn(name = "pro_id")
    @JsonIgnore
    private Product product;
}
