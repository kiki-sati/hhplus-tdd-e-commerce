package com.hhp.ecommerce.interfaces.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hhp.ecommerce.interfaces.dto.CartItemResponse;

@RestController
@RequestMapping("/cart")
public class CartController {

    private static final List<CartItemResponse> cartItems = new ArrayList<>();

    @PostMapping("/add")
    public List<CartItemResponse> addItemToCart(@RequestBody CartItemResponse item) {
        cartItems.add(item);
        return cartItems;
    }

    @DeleteMapping("/remove")
    public List<CartItemResponse> removeItemFromCart(@RequestParam UUID productId) {
        cartItems.removeIf(item -> item.getProductId().equals(productId));
        return cartItems;
    }

    @GetMapping
    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }
}