package com.agricart.app.cart;

import com.agricart.app.auth.UserService;
import com.agricart.app.cart.payload.AddItemRequest;
import com.agricart.app.cart.payload.CartRespond;
import com.agricart.app.cart.payload.RemoveCartItemRequest;
import com.agricart.app.common.NotFoundException;
import com.agricart.app.common.Status;
import com.agricart.app.item.ItemEntity;
import com.agricart.app.item.ItemRepository;
import com.agricart.app.item.ItemService;
import com.agricart.app.utility.SmsHandler;
import com.agricart.app.utility.SmsKeyEntity;
import com.agricart.app.utility.SmsKeyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ShoppingCartService {

    private final ItemService itemService;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ItemRepository itemRepository;
    private final SmsKeyRepository smsKeyRepository;
    private final SmsHandler smsHandler;

    public CartRespond viewShoppingCart(String username) {

        ShoppingCartEntity cart =  shoppingCartRepository.findByUserIdAndPurchase(userService.getAuthUserId(username),false)
                                                            .orElse(new ShoppingCartEntity());

        if(cart.getUserId() <= 0 ){
            return CartRespond.builder()
                    .cartId(cart.getId())
                    .items(new ArrayList<>())
                    .build();
        }else{

            List<CartItemEntity> items = cartItemRepository.findAllByCartIdId(cart.getId());
            return CartRespond.builder()
                    .cartId(cart.getId())
                    .items(items)
                    .build();
        }
    }


    @Transactional
    public CartItemEntity addItemToCart(AddItemRequest request, String username) {

        // check if user has a active cart
        ShoppingCartEntity cart =  shoppingCartRepository.findByUserIdAndPurchase(userService.getAuthUserId(username),false)
                                                            .orElse(new ShoppingCartEntity());

        // if not cart exist
        if(cart.getUserId() <= 0 ){

            //config new cart
            cart.setUserId(userService.getAuthUserId(username));
            cart.setCreatedAt(new Date());
            cart.setCreatedBy(userService.getAuthUserId(username));

            // save updated cart to same variable
            cart = shoppingCartRepository.save(cart);

            //create cart items

            ItemEntity item = itemService.viewItemById(request.getItemId());
            item.subQuantity(request.getQuantity());

            //override old item with new object
            item = itemRepository.save(item);

            CartItemEntity cartItemToBeAdd = CartItemEntity.builder()
                                                    .cartId(cart)
                                                    .itemId(item)
                                                    .quantity(request.getQuantity())
                                                    .build();


            cartItemToBeAdd =  cartItemRepository.save(cartItemToBeAdd);
            return cartItemToBeAdd;

        }else{

            if(cart.getStatus().equals(Status.Cart.PENDINGFORCONF)){
                throw new RuntimeException("please conform your payment");
            }

            cart.setModifiedAt(new Date());
            cart.setModifiedBy(userService.getAuthUserId(username));

            //save updated cart
            cart = shoppingCartRepository.save(cart);

            // get list of cart items
            List<CartItemEntity> itemsInCart = cartItemRepository.findAllByCartIdId(cart.getId());

            // check if item exist or not in this cart
            CartItemEntity itemEntity = itemsInCart.stream()
                                            .filter(element -> element.getItemId().getId() == request.getItemId())
                                            .findAny()
                                            .orElse(null);

            if(itemEntity == null){

                ItemEntity item = itemRepository.findById(request.getItemId()).orElseThrow(()->new NotFoundException("Item not found"));
                item.subQuantity(request.getQuantity());

                item = itemRepository.save((item));

                CartItemEntity cartItemToBeAdd = CartItemEntity.builder()
                        .cartId(cart)
                        .itemId(item)
                        .quantity(request.getQuantity())
                        .build();

                return cartItemRepository.save(cartItemToBeAdd);

            }else{

                ItemEntity item = itemEntity.getItemId();
                item.addQuantity(itemEntity.getQuantity());
                item.subQuantity(request.getQuantity());

                item = itemRepository.save(item);

                itemEntity.setItemId(item);
                itemEntity.setQuantity(request.getQuantity());

                return cartItemRepository.save(itemEntity);

            }

        }

    }

    @Transactional
    public void removeItemFromCart(RemoveCartItemRequest request, String username) {

        // check if active cart available for user
        ShoppingCartEntity cart =  shoppingCartRepository.findByUserIdAndPurchase(userService.getAuthUserId(username),false)
                                                            .orElseThrow(()->new NotFoundException("item not found"));

        if(cart.getStatus().equals(Status.Cart.PENDINGFORCONF)){
            throw new RuntimeException("please conform your payment");
        }

        // check remove to be item is in the cart
        CartItemEntity cartItem = cartItemRepository.findByCartIdIdAndItemIdId(cart.getId() , request.getItemId())
                                                            .orElseThrow(()->new NotFoundException("Cart item not found"));

        ItemEntity item = itemRepository.findById(request.getItemId()).get();

        item.addQuantity(cartItem.getQuantity());

        itemRepository.save(item);

        cartItemRepository.delete(cartItem);

        // delete cart when items are 0
        if(cartItemRepository.findAllByCartIdId(cart.getId()).stream().count() == 0){
            shoppingCartRepository.delete(cart);
        }

    }


    public void updatePayment(long cartId , float amount ,  String type , String phone){

        ShoppingCartEntity cart = shoppingCartRepository.findById(cartId).orElseThrow(()->new NotFoundException("requested cart not found"));


        if(cart.getStatus().equals(Status.Cart.PENDINGFORCONF)){
            throw new RuntimeException("please conform your payment");
        }

        double cartPrice = cartItemRepository.findAllByCartIdId(cartId)
                                                .stream()
                                                .mapToDouble(element -> element.getItemId().getPrice() * element.getQuantity())
                                                .sum();

    }

    public List<ShoppingCartEntity> viewPurchaseHistory(String username) {
        return shoppingCartRepository.findAllByUserId(userService.getAuthUserId(username));
    }
}
