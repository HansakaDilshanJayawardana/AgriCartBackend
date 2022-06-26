package com.agricart.app.item.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class ItemCreateRequest {

    private long subCategoryId;
    private String itemName;
    private float price;
    private String description;
    private int quantity;
    private String itemType;
    private Date expDate;
    private String url;
}
