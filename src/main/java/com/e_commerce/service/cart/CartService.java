package com.e_commerce.service.cart;

import com.e_commerce.dto.cart.CartDTO;
import com.e_commerce.dto.cart.CartOrderSizeVariantDTO;
import com.e_commerce.entity.SizeVariant;

public interface CartService {
     CartDTO getCartForCurrentUser();
     CartDTO addItemToCart(Long sizeVariantId, Integer quantity);
     CartDTO removeItemFromCart(Long cartItemId);
     CartDTO updateItemQuantity(Long cartItemId, Integer quantity);
     CartDTO clearCart();
     CartOrderSizeVariantDTO toCartOrderSizeVariantDTO(SizeVariant sizeVariant);
     void validateAndReduceStockForOrder();
    }
