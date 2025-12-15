package com.wearetrying.space_cats_market.services;
import com.wearetrying.space_cats_market.domain.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    void deleteById(Long id);
}
