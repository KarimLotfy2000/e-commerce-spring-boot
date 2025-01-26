package com.e_commerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private Double subtotal;


    @ManyToOne
    @JoinColumn(name = "size_variant_id", nullable = false)
    private SizeVariant sizeVariant;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id",nullable = false)
    private Cart cart;


    @PrePersist
    @PreUpdate
    public void calculateSubTotal() {
        if (quantity != null && sizeVariant != null && sizeVariant.getPrice() != null) {
         setSubtotal(quantity * sizeVariant.getPrice());
        } else {
            this.subtotal = 0.0;
        }
    }

}
