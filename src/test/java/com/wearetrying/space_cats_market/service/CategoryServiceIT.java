package com.wearetrying.space_cats_market.service;

import com.wearetrying.space_cats_market.AbstractIt;
import com.wearetrying.space_cats_market.domain.Category;
import com.wearetrying.space_cats_market.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@WithMockUser(username = "admin", roles = "ADMIN")
public class CategoryServiceIT extends AbstractIt {

    @Autowired
    private CategoryService categoryService;

    @Test
    @Transactional
    void shouldSaveAndFindCategory() {
        Category newCategory = new Category();
        newCategory.setName("Cyber Implants");

        Category saved = categoryService.save(newCategory);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Cyber Implants", saved.getName());

        // Перевірка через findById
        Category found = categoryService.findById(saved.getId());
        Assertions.assertEquals("Cyber Implants", found.getName());
    }

    @Test
    @Transactional
    void shouldDeleteCategory() {
        Category category = new Category();
        category.setName("Trash");
        Category saved = categoryService.save(category);
        Long id = saved.getId();

        categoryService.deleteById(id);

        Assertions.assertThrows(RuntimeException.class, () -> categoryService.findById(id));
    }

    @Test
    @Transactional
    void shouldFindAll() {
        categoryService.save(createCategory("Cat 1"));
        categoryService.save(createCategory("Cat 2"));

        List<Category> all = categoryService.findAll();
        Assertions.assertTrue(all.size() >= 2);
    }

    private Category createCategory(String name) {
        Category c = new Category();
        c.setName(name);
        return c;
    }
}