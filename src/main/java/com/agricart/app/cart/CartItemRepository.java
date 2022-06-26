package com.agricart.app.cart;

import com.agricart.app.cart.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity , Long> {
    List<CartItemEntity> findAllByCartId(long id);

    List<CartItemEntity> findAllByCartIdId(long id);

    Optional<CartItemEntity> findByCartIdIdAndItemIdId(long id, long itemId);
}
