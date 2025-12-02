package com.wearetrying.space_cats_market.service;

import com.wearetrying.space_cats_market.service.exception.FeatureNotAvailableException;
import com.wearetrying.space_cats_market.service.FeatureToggleService;
import com.wearetrying.space_cats_market.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class CosmoCatServiceTest {

    @Autowired
    private CosmoCatService cosmoCatService;

    @MockitoBean
    private FeatureToggleService featureToggleService;

    @MockitoBean
    private ProductService productService;

    private List<Product> mockProducts;

    @BeforeEach
    void setUp() {
        mockProducts = List.of(
                Product.builder()
                        .id(5L)
                        .name("Test SpaceCat")
                        .description("Test SpaceCat description")
                        .price(BigDecimal.valueOf(100.0))
                        .category("Electronics")
                        .stockQuantity(50)
                        .build()
        );
    }

    // --- ТЕСТИ ДЛЯ getCosmoCatalog (фіча: cosmoProducts) ---

    @Test
    @DisplayName("Must return products if cosmoProducts enable")
    void testGetCosmoCatalog_Enabled() {
        when(featureToggleService.check("cosmoProducts")).thenReturn(true);
        when(productService.getAllProducts()).thenReturn(mockProducts);

        List<Product> result = cosmoCatService.getCosmoCatalog();

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Test SpaceCat", result.get(0).getName());
        verify(productService, times(1)).getAllProducts(); // Перевіряємо, що базу дійсно викликали
    }

    @Test
    @DisplayName("Must throw exception if cosmoProducts disabled")
    void testGetCosmoCatalog_Disabled() {
        when(featureToggleService.check("cosmoProducts")).thenReturn(false);

        Assertions.assertThrows(FeatureNotAvailableException.class, () -> {
            cosmoCatService.getCosmoCatalog();
        });

        verify(productService, never()).getAllProducts();
    }

    // --- ТЕСТИ ДЛЯ getBlackHoleDeals (фіча: blackHoleDiscounts) ---

    @Test
    @DisplayName("Should apply 99% discount if blackHoleDiscounts feature is enabled")
    void testGetBlackHoleDeals_Enabled() {
        when(featureToggleService.check("blackHoleDiscounts")).thenReturn(true);
        when(productService.getAllProducts()).thenReturn(mockProducts);

        List<Product> result = cosmoCatService.getBlackHoleDeals();

        Product discountedProduct = result.get(0);
        Assertions.assertTrue(discountedProduct.getName().contains("BLACK HOLE SALE"));

        BigDecimal expectedPrice = BigDecimal.valueOf(1.00);
        Assertions.assertEquals(0, expectedPrice.compareTo(discountedProduct.getPrice()));
    }

    @Test
    @DisplayName("Must throw exception if blackHoleDiscounts disabled")
    void testGetBlackHoleDeals_Disabled() {
        when(featureToggleService.check("blackHoleDiscounts")).thenReturn(false);

        Assertions.assertThrows(FeatureNotAvailableException.class, () -> {
            cosmoCatService.getBlackHoleDeals();
        });

        verify(productService, never()).getAllProducts();
    }
}
