package com.agricart.app.item;

import com.agricart.app.auth.UserService;
import com.agricart.app.common.FieldValidationException;
import com.agricart.app.common.NotFoundException;
import com.agricart.app.item.payload.ItemCreateRequest;
import com.agricart.app.utility.ItemTypes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemCategoryRepository itemCategoryRepository;

    public ArrayList<String> itemTypeList(){
//        ArrayList<String> itemTypes = new ArrayList<>();
//        itemTypes.add(ItemTypes.FOOD);
//        itemTypes.add(ItemTypes.FRUIT);
//        itemTypes.add(ItemTypes.PLANT);
//        itemTypes.add(ItemTypes.SEEDS);
//
//        return itemTypes;

        return itemCategoryRepository.findAllCategory();
    }

    public List fetchAllItems() {
        return itemRepository.findAll();
    }

    public List frtchItemsByNameContains(String searchKey) {
        return itemRepository.findByItemNameContainingIgnoreCase(searchKey);
    }

    public ItemEntity createItem(ItemCreateRequest itemCreateRequest , String username) {

        ItemEntity saveToBe = ItemEntity.builder()
                                        .itemName(itemCreateRequest.getItemName())
                                        .price(itemCreateRequest.getPrice())
                                        .description(itemCreateRequest.getDescription())
                                        .quantity(itemCreateRequest.getQuantity())
                                        .itemType(itemCreateRequest.getItemType())
                                        .expDate(itemCreateRequest.getExpDate())
                .url(itemCreateRequest.getUrl())
                                        .build();
        // set auditable
        saveToBe.setCreatedBy(userService.getAuthUserId(username));
        saveToBe.setCreatedAt(new Date());

        return itemRepository.save(saveToBe);

    }

    public ItemEntity updateItem(ItemCreateRequest itemCreateRequest , long id ){



        ItemEntity updateToBe = itemRepository.findById(id).orElseThrow(()->new RuntimeException("Object not found"));

        // check auth user have permission to update current object
//        if(updateToBe.getCreatedBy() != userService.getAuthUserId(username)){
//            throw new FieldValidationException("current user have no authorize to update");
//        }

        updateToBe.setItemName(itemCreateRequest.getItemName());
        updateToBe.setPrice(itemCreateRequest.getPrice());
        updateToBe.setDescription(itemCreateRequest.getDescription());
        updateToBe.setQuantity(itemCreateRequest.getQuantity());
        updateToBe.setItemType(itemCreateRequest.getItemType());
        updateToBe.setUrl(itemCreateRequest.getUrl());
        updateToBe.setExpDate(itemCreateRequest.getExpDate());

        // set auditable
//        updateToBe.setModifiedBy(userService.getAuthUserId(username));
        updateToBe.setModifiedAt(new Date());

        return itemRepository.save(updateToBe);

    }

    public void deleteItem(long id) {
        ItemEntity deleteToBe = itemRepository.findById(id).orElseThrow(()->new RuntimeException("Object not found"));

        // check auth user have permission to update current object


        itemRepository.delete(deleteToBe);
    }

    public ItemEntity viewItemById(long id){
        return itemRepository.getById(id);
    }

    public ItemEntity getItemById(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(()->new NotFoundException("Item not found exception"));
    }

    public ItemCategory createItemType(ItemCategory category) {
        return itemCategoryRepository.save(category);
    }
}
