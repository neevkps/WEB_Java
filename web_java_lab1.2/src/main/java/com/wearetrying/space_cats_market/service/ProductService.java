package com.wearetrying.space_cats_market.service;
import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.dto.product.ProductDetailsEntry;


import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}