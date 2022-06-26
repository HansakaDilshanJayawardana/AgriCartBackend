package com.agricart.app.cart;

import com.agricart.app.cart.payload.AddItemRequest;
import com.agricart.app.cart.payload.CartRespond;
import com.agricart.app.common.Status;
import com.agricart.app.cart.payload.RemoveCartItemRequest;
import com.agricart.app.common.FieldValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("api/v2/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;


    @GetMapping("/view")
    public ResponseEntity<CartRespond> viewShoppingCart(@AuthenticationPrincipal UserDetails user){
        return new ResponseEntity<>(shoppingCartService.viewShoppingCart(user.getUsername()) , HttpStatus.OK);
    }

    @PostMapping("/addItem")
    public ResponseEntity<CartItemEntity> addItemsToShoppingCart(@RequestBody AddItemRequest request , @AuthenticationPrincipal UserDetails user){
        //basic controller level validation
        if(request.getItemId() <= 0){
            throw new FieldValidationException("give proper item id");
        }
        if(request.getQuantity() < 0){
            throw new FieldValidationException("give proper quantity");
        }

        return new ResponseEntity<>(shoppingCartService.addItemToCart(request , user.getUsername()) , HttpStatus.CREATED);

    }

    @DeleteMapping("/removeItem")
    public ResponseEntity<Object> removeCartItem(@RequestBody RemoveCartItemRequest request , @AuthenticationPrincipal UserDetails user){

        //basic controller level validations
        if(request.getItemId() <= 0 ){
            throw new FieldValidationException("item id is required");
        }

        shoppingCartService.removeItemFromCart(request , user.getUsername());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/history")
    public List<ShoppingCartEntity> viewPurchaseHistory(@AuthenticationPrincipal UserDetails user){
        return shoppingCartService.viewPurchaseHistory(user.getUsername());
    }
}
