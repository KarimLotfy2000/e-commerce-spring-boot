package com.e_commerce.controller;

import com.e_commerce.dto.cart.CartDTO;
import com.e_commerce.response.ApiResponse;
import com.e_commerce.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartDTO>> getCart() {
        CartDTO cartDTO = cartService.getCartForCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>("Cart retrieved successfully", cartDTO));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartDTO>> addItemToCart(
            @RequestParam Long sizeVariantId,
            @RequestParam Integer quantity) {
        CartDTO updatedCart = cartService.addItemToCart(sizeVariantId, quantity);
        return ResponseEntity.ok(new ApiResponse<>("Item added to cart successfully", updatedCart));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<ApiResponse<CartDTO>> removeItemFromCart(@PathVariable Long cartItemId) {
        CartDTO updatedCart = cartService.removeItemFromCart(cartItemId);
        return ResponseEntity.ok(new ApiResponse<>("Item removed from cart successfully", updatedCart));
    }

    @PatchMapping("/update-quantity/{cartItemId}")
    public ResponseEntity<ApiResponse<CartDTO>> updateItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        CartDTO updatedCart = cartService.updateItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok(new ApiResponse<>("Item quantity updated successfully", updatedCart));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<CartDTO>> clearCart() {
        CartDTO clearedCart = cartService.clearCart();
        return ResponseEntity.ok(new ApiResponse<>("Cart cleared successfully", clearedCart));
    }

    @PostMapping("/validate-and-reduce-stock")
    public ResponseEntity<ApiResponse<String>> validateAndReduceStockForOrder() {
        cartService.validateAndReduceStockForOrder();
        return ResponseEntity.ok(new ApiResponse<>("Stock validated and reduced successfully", "Order is ready to be processed"));
    }
}
