package com.wearetrying.space_cats_market.services.impl;
import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.entity.ProductEntity;
import com.wearetrying.space_cats_market.projection.TopProductProjection;
import com.wearetrying.space_cats_market.repository.ProductRepository;
import com.wearetrying.space_cats_market.services.ProductService;
import com.wearetrying.space_cats_market.services.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Override
    @Transactional
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public List<TopProductProjection> getTopPurchasedProducts() {
        return repository.findMostPurchasedProducts();
    }
}
