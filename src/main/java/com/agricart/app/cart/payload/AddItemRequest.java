package com.agricart.app.cart.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddItemRequest {
    private long itemId;
    private int quantity;
}
