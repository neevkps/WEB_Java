package com.wearetrying.space_cats_market.services.impl;

import com.wearetrying.space_cats_market.domain.Category;
import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.dto.ProductDetailsDto;
import com.wearetrying.space_cats_market.entity.ProductEntity;
import com.wearetrying.space_cats_market.projection.TopProductProjection;
import com.wearetrying.space_cats_market.repository.CategoryRepository;
import com.wearetrying.space_cats_market.repository.ProductRepository;
import com.wearetrying.space_cats_market.services.ProductService;
import com.wearetrying.space_cats_market.services.mapper.CategoryMapper;
import com.wearetrying.space_cats_market.services.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    @PostFilter("hasRole('ADMIN') or filterObject.stockQuantity > 0")
    public List<Product> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Product findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }


    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Product createProduct(ProductDetailsDto dto) {
        Product domainProduct = mapper.toDomain(dto);

        Category category = categoryRepository.findByName(dto.getCategory())
                .map(categoryMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategory()));

        domainProduct.setCategory(category);

        return this.save(domainProduct);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<TopProductProjection> getTopPurchasedProducts() {
        return repository.findMostPurchasedProducts();
    }
}