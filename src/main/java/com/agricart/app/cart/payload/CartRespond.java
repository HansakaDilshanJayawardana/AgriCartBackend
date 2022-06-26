package com.agricart.app.cart.payload;

import com.agricart.app.cart.CartItemEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRespond {
    private long cartId;
    private List<CartItemEntity> items = new ArrayList<>();

    public double getCartPrice() {
        if (!this.items.isEmpty()) {
            return this.items.stream().mapToDouble(value -> value.getItemId().getPrice() * value.getQuantity()).sum();
        } else {
            return 0;
        }
    }

}
