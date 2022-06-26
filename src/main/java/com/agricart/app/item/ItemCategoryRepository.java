package com.agricart.app.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory , Long> {

    @Query(value = "select t.categoryName from ItemCategory t ")
    ArrayList<String> findAllCategory();

}
