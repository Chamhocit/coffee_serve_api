package com.aptech.shop.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="product")
@Data
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pro_name")
    private String name;

    @Column(name = "pro_desciption")
    private String description;

    @Column(name = "pro_sold")
    private int sold;

    @Column(name = "pro_quantity")
    private int quantity;

    @Column(name = "category")
    private String category;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductImage img;
}
