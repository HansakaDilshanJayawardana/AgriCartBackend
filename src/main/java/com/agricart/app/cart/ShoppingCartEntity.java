package com.agricart.app.cart;

import com.agricart.app.auth.UserEntity;
import com.agricart.app.common.Status;
import com.agricart.app.item.ItemEntity;
import com.agricart.app.utility.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id" , nullable = false)
    private long userId;

    @Column(name = "is_purchase"  ,nullable = false)
    private boolean purchase = false;

    @Column(name = "status")
    private String status = Status.Cart.PENDING;

}
