package com.e_commerce.entity;

import com.e_commerce.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String brand;
    private String material;
    private String color;
    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    private Gender gender;
    private Double price;
    private LocalDateTime createdAt;


    @ElementCollection()
    @Column(name = "image_url")

    private List<String> images;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<SizeVariant> sizeVariants;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
