package com.agricart.app.item;

import com.agricart.app.utility.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false , name = "item_name")
    private String itemName;

    @Column(name = "price" , nullable = false)
    @Min(0)
    private float price;

    @Column(nullable = false)
    private String description;

    @Column(name = "quantity" , nullable = false)
    @Min(0)
    private int quantity;

    @Column(name = "item_type" , nullable = false)
    private String itemType;

    @Column(name = "exp_date" , nullable = true)
    private Date expDate;

    @Column(name = "url" , length = 10000)
    private String url;

    public void addQuantity(int quantity){
        this.quantity += quantity;
    }

    public void subQuantity(int quantity){
        if((this.quantity - quantity) < 0){
            throw new RuntimeException("requested quantity can not be issue");
        }else{
            this.quantity -= quantity;
        }
    }
}
