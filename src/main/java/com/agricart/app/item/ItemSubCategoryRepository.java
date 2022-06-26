package com.agricart.app.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemSubCategoryRepository extends JpaRepository<ItemSubCategoryEntity , Long> {
    List<ItemSubCategoryEntity> findAllByCategory(String toUpperCase);
}
