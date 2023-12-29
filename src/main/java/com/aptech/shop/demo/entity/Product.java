package com.aptech.shop.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

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

    @Column(name = "pro_price")
    private Double proPrice;

    @Column(name = "pro_desciption")
    private String description;

    @Column(name = "pro_sold")
    private Integer sold;

    @Column(name = "pro_quantity")
    private Integer quantity;

    @Column(name = "category")
    private String category;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToOne(mappedBy = "product", fetch = FetchType.EAGER)
    private ProductImage img;
}
