package com.agricart.app.item;

import com.agricart.app.common.FieldValidationException;
import com.agricart.app.item.payload.ItemCreateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v2/item")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/itemTypes")
    public ResponseEntity<ArrayList<String>> viewItemTypes(){
        return new ResponseEntity<>(itemService.itemTypeList() , HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List> itemList(@RequestParam(name = "name" , required = false , defaultValue = "") String searchKey){

        if(searchKey.isEmpty()){
            return new ResponseEntity<>(itemService.fetchAllItems() , HttpStatus.OK);
        }else{
            return new ResponseEntity<>(itemService.frtchItemsByNameContains(searchKey) , HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<ItemEntity> createItem(@RequestBody ItemCreateRequest itemCreateRequest , @AuthenticationPrincipal UserDetails user){
        // basic controller level validations
        if(itemCreateRequest.getItemName().isEmpty()){
            throw new FieldValidationException("item name is required");
        }
        if(itemCreateRequest.getItemName().length() < 3) {
            throw new FieldValidationException("item name must be more than 3 characters");
        }
        if(itemCreateRequest.getPrice() < 0f) {
            throw new FieldValidationException("item price must be grater than 0");
        }
        if(itemCreateRequest.getQuantity() < 0) {
            throw new FieldValidationException("item quantity must be grater than 0");
        }

        if(itemCreateRequest.getExpDate() != null){
            if(itemCreateRequest.getExpDate().before(new Date())){
                throw new FieldValidationException("item exp date must be future date from today");
            }
        }

        return new ResponseEntity<>(itemService.createItem(itemCreateRequest , user.getUsername()) , HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<ItemEntity> updateItem(@RequestBody ItemCreateRequest itemCreateRequest , @RequestParam(name = "id") long id , @AuthenticationPrincipal UserDetails user ){

        // basic controller level validations
        if(itemCreateRequest.getItemName().isEmpty()){
            throw new FieldValidationException("item name is required");
        }
        if(itemCreateRequest.getItemName().length() < 3) {
            throw new FieldValidationException("item name must be more than 3 characters");
        }
        if(itemCreateRequest.getPrice() < 0f) {
            throw new FieldValidationException("item price must be grater than 0");
        }
        if(itemCreateRequest.getQuantity() < 0) {
            throw new FieldValidationException("item quantity must be grater than 0");
        }

        if(itemCreateRequest.getExpDate() != null){
            if(itemCreateRequest.getExpDate().before(new Date())){
                throw new FieldValidationException("item exp date must be future date from today");
            }
        }


        return new ResponseEntity<>(itemService.updateItem(itemCreateRequest , id) , HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<Object> deleteItem(@RequestParam(name = "id") long id , @AuthenticationPrincipal UserDetails user){
        itemService.deleteItem(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @PostMapping("/itemTypes")
    public ResponseEntity<ItemCategory> createItemType(@RequestBody ItemCategory itemCategory){
        if(itemCategory.getCategoryName().isEmpty()){
            throw new FieldValidationException("item category is required");
        }
        return new ResponseEntity<>(itemService.createItemType(itemCategory) , HttpStatus.CREATED);
    }

    @GetMapping("/viewItem/{itemId}")
    public ResponseEntity<ItemEntity> viewItem(@PathVariable long itemId){
        return new ResponseEntity<>(itemService.getItemById(itemId) , HttpStatus.OK);
    }
}
