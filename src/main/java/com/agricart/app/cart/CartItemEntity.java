package com.agricart.app.cart;

import com.agricart.app.item.ItemEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cart_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private ShoppingCartEntity cartId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity itemId;

    @Column(name = "quantity")
    private int quantity;

}
