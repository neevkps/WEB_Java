package com.wearetrying.space_cats_market.service;

import com.wearetrying.space_cats_market.config.MappersTestConfiguration;
import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.service.exception.ProductNotFoundException;
import com.wearetrying.space_cats_market.service.exception.ValidationException;
import com.wearetrying.space_cats_market.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = {ProductServiceImpl.class})
@Import(MappersTestConfiguration.class)
@DisplayName("Product Service Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceTest {
    private static final String PRODUCT_NAME = "SpaceProductTestName";
    private static final String PRODUCT_DESCRIPTION = "Space Product Description";
    private static final double PRODUCT_PRICE = 999.9;
    private static final String PRODUCT_CATEGORY = "TestCategory";
    private static final int PRODUCT_STOCK_QUANTITY = 10;


    @Autowired
    private ProductServiceImpl productService;

    private Product buildProduct() {
        return Product.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(BigDecimal.valueOf(PRODUCT_PRICE))
                .category(PRODUCT_CATEGORY)
                .stockQuantity(PRODUCT_STOCK_QUANTITY)
                .build();
    }


    @Test
    @DisplayName("Should add a new product")
    public void shouldAddNewProduct() {
        Product newProduct = buildProduct();
        Product added = productService.createProduct(newProduct);

        assertNotNull(added.getId());
        assertEquals(PRODUCT_NAME, added.getName());
        assertEquals(PRODUCT_DESCRIPTION, added.getDescription());
        assertEquals(BigDecimal.valueOf(PRODUCT_PRICE), added.getPrice());
        assertEquals(PRODUCT_CATEGORY, added.getCategory());
        assertEquals(PRODUCT_STOCK_QUANTITY, added.getStockQuantity());

        Product fetched = productService.getProductById(added.getId());
        assertNotNull(fetched);
        assertEquals(added, fetched);

        assertTrue(productService.getAllProducts().contains(fetched));
    }
    @Test
    @DisplayName("Get all products test")
    public void shouldGetAllProducts() {
        List<Product> allProducts = productService.getAllProducts();
        assertNotNull(allProducts);
        assertEquals(allProducts.size(), 3);

        Product product1 = allProducts.get(0);
        Product product2 = allProducts.get(1);

        assertTrue(allProducts.contains(product1));
        assertTrue(allProducts.contains(product2));
    }
    @Test
    @DisplayName("Get product by id test")
    public void shouldGetProductById() {
        Product fetched = productService.getProductById(2L);
        assertNotNull(fetched);
        assertEquals(2L, fetched.getId());
        assertEquals("Galaxy Smartphone", fetched.getName());
        assertEquals("Smartphone with cosmic connectivity", fetched.getDescription());
        assertEquals(BigDecimal.valueOf(899.50), fetched.getPrice());
        assertEquals("Electronics", fetched.getCategory());
        assertEquals(50, fetched.getStockQuantity());
    }
    @Test
    @DisplayName("Update product test")
    public void shouldUpdateProduct() {
        Product existingProduct = productService.getProductById(1L);

        Product updatedData = Product.builder()
                .name("Updated Monitor Name")
                .description("Updated description for monitor")
                .price(BigDecimal.valueOf(2500.00))
                .category("Updated Electronics")
                .stockQuantity(20)
                .build();

        Product updatedProduct = productService.updateProduct(existingProduct.getId(), updatedData);

        assertEquals(1L, updatedProduct.getId());

        assertEquals("Updated Monitor Name", updatedProduct.getName());
        assertEquals("Updated description for monitor", updatedProduct.getDescription());
        assertEquals(BigDecimal.valueOf(2500.00), updatedProduct.getPrice());
        assertEquals("Updated Electronics", updatedProduct.getCategory());
        assertEquals(20, updatedProduct.getStockQuantity());

        Product fetched = productService.getProductById(1L);
        assertEquals(updatedProduct, fetched);
        assertEquals("Updated Monitor Name", fetched.getName());
    }

    @Test
    @DisplayName("Delete product test")
    public void shouldDeleteProduct() {
        Product productToDelete = productService.getProductById(3L);

        assertTrue(productService.getAllProducts().contains(productToDelete));
        productService.deleteProduct(3L);

        assertFalse(productService.getAllProducts().contains(productToDelete));
    }
    @Test
    @DisplayName("Throwing exception when product with id not found test")
    public void shouldThrowExceptionGetProductById() {
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(35L));
    }
    @Test
    @DisplayName("Update non-existing product should throw")
    void shouldThrowWhenUpdateNonExisting() {
        Product updatedData = Product.builder()
                .name("Updated")
                .description("Updated desc")
                .price(BigDecimal.valueOf(100))
                .category("Electronics")
                .stockQuantity(5)
                .build();

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(999L, updatedData));
    }
    @Test
    @DisplayName("Should throw custom ValidationException when business rules are violated")
    void shouldThrowBusinessValidationException() {
        Product badProduct = buildProduct();
        badProduct.setStockQuantity(5000);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            productService.createProduct(badProduct);
        });


        assertNotNull(exception.getValidationErrors());
        String allErrors = String.join(", ", exception.getValidationErrors());

        assertTrue(allErrors.contains("Warehouse capacity exceeded"));
    }
}
