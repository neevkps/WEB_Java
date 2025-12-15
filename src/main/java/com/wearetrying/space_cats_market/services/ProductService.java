package com.wearetrying.space_cats_market.services;

import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.projection.TopProductProjection;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    void deleteById(Long id);
    List<TopProductProjection> getTopPurchasedProducts();
}