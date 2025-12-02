package com.wearetrying.space_cats_market.service;
import com.wearetrying.space_cats_market.annotation.FeatureToggle;
import com.wearetrying.space_cats_market.domain.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CosmoCatService {
    private final ProductService productService;

    public CosmoCatService(ProductService productService) {
        this.productService = productService;
    }

    @FeatureToggle("cosmoProducts")
    public List<Product> getCosmoCatalog() {
        return productService.getAllProducts();
    }

    @FeatureToggle("blackHoleDiscounts")
    public List<Product> getBlackHoleDeals() {
        List<Product> originalProducts = productService.getAllProducts();
        return originalProducts.stream()
                .map(product -> {
                    Product discountedProduct = product.toBuilder().build();
                    BigDecimal newPrice = product.getPrice().multiply(BigDecimal.valueOf(0.01));
                    discountedProduct.setPrice(newPrice);
                    discountedProduct.setName(product.getName() + " (BLACK HOLE SALE)");

                    return discountedProduct;
                })
                .collect(Collectors.toList());
    }
}
