package com.agricart.app.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Long> {


    Optional<ShoppingCartEntity> findByUserIdAndPurchase(long authUserId, boolean b);

    Optional<ShoppingCartEntity> findByIdAndPurchase(long cartId, boolean b);

    List<ShoppingCartEntity> findAllByUserId(long authUserId);
}
