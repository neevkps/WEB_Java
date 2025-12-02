package com.wearetrying.space_cats_market.service.impl;

import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.service.ProductService;
import com.wearetrying.space_cats_market.service.exception.ProductNotFoundException;

import com.wearetrying.space_cats_market.service.exception.ValidationException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;


@Service
public class ProductServiceImpl implements ProductService {

    private final List<Product> products = new ArrayList<>();

    public ProductServiceImpl() {
        createProduct(Product.builder()
                .name("Starship Monitor")
                .description("High-end monitor for interstellar missions")
                .price(BigDecimal.valueOf(1999.99))
                .category("Electronics")
                .stockQuantity(10)
                .build());

        createProduct(Product.builder()
                .name("Galaxy Smartphone")
                .description("Smartphone with cosmic connectivity")
                .price(BigDecimal.valueOf(899.50))
                .category("Electronics")
                .stockQuantity(50)
                .build());
        createProduct(Product.builder()
                .name("Cosmo Cat")
                .description("some description")
                .price(BigDecimal.valueOf(555.50))
                .category("Toys")
                .stockQuantity(5)
                .build());
    }
    private static long idCounter = 1;
    @Override
    public Product createProduct(Product product) {
        product.setId(idCounter++);
        validateProductBusinessRules(product);
        products.add(product);
        return product;
    }


    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    @Override
    public Product getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);

        Product updated = existingProduct.toBuilder()
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .price(updatedProduct.getPrice())
                .category(updatedProduct.getCategory())
                .stockQuantity(updatedProduct.getStockQuantity())
                .build();
        validateProductBusinessRules(updatedProduct);
        int index = products.indexOf(existingProduct);
        products.set(index, updated);

        return updated;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        products.remove(product);
    }
    private void validateProductBusinessRules(Product product) {
        List<String> errors = new ArrayList<>();
        if (product.getStockQuantity() > 1000) {
            errors.add("Warehouse capacity exceeded (Max 1000)");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
