package com.agricart.app.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity , Long> {
    List findAllByItemNameContaining(String searchKey);

    List findByItemNameContainingIgnoreCase(String searchKey);
}
