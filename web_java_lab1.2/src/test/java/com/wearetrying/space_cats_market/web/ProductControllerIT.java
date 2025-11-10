package com.wearetrying.space_cats_market.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.dto.product.ProductDetailsDto;
import com.wearetrying.space_cats_market.service.ProductService;
import com.wearetrying.space_cats_market.service.exception.ProductNotFoundException;
import com.wearetrying.space_cats_market.service.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Product Controller IT")
@Tag("product-service")
public class ProductControllerIT {

    private static final String PRODUCT_NAME = "Galaxy SpaceProductTestName";
    private static final String PRODUCT_DESCRIPTION = "Space Product Description";
    private static final double PRODUCT_PRICE = 999.9;
    private static final String PRODUCT_CATEGORY = "TestCategory";
    private static final int PRODUCT_STOCK_QUANTITY = 10;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @TestConfiguration
    static class MockProductServiceConfig {
        @Bean
        public ProductService productService() {
            return mock(ProductService.class);
        }
    }
    @BeforeEach
    void setUp() {
        Mockito.reset(productService);
    }

    private static ProductDetailsDto buildProductDto() {
        return ProductDetailsDto.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(BigDecimal.valueOf(PRODUCT_PRICE))
                .category(PRODUCT_CATEGORY)
                .stockQuantity(PRODUCT_STOCK_QUANTITY)
                .build();
    }

    @Test
    void testCreateProduct_validDto_returnsCreated() throws Exception {
        ProductDetailsDto productDto = buildProductDto();

        Set<ConstraintViolation<ProductDetailsDto>> violations =
                Validation.buildDefaultValidatorFactory()
                        .getValidator()
                        .validate(productDto);
        assertTrue(violations.isEmpty(), "DTO повинен бути валідним");

        Product product = productMapper.toProduct(productDto);

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.description").value(PRODUCT_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(PRODUCT_PRICE))
                .andExpect(jsonPath("$.category").value(PRODUCT_CATEGORY));

        verify(productService, times(1)).createProduct(any(Product.class));
    }
    @Test
    void testCreateProduct_invalidDto_returnsBadRequest() throws Exception {
        ProductDetailsDto invalidDto = ProductDetailsDto.builder()
                .name("")
                .description("Valid description")
                .price(BigDecimal.ZERO)
                .category("Some Category")
                .stockQuantity(-5)
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("validation_error"))
                .andExpect(jsonPath("$.detail", containsString("Product name is mandatory")))
                .andExpect(jsonPath("$.detail", containsString("Price must be greater than 0")))
                .andExpect(jsonPath("$.detail", containsString("Stock quantity must be non-negative")));

        verify(productService, never()).createProduct(any(Product.class));
    }

    @Test
    void testGetAllProducts() throws Exception {
        ProductDetailsDto dto = buildProductDto();
        Product product = productMapper.toProduct(dto);

        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productDetailsDto[0].name").value(PRODUCT_NAME));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById() throws Exception {
        ProductDetailsDto dto = buildProductDto();
        Product product = productMapper.toProduct(dto);

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/v1/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductDetailsDto updatedDto = ProductDetailsDto.builder()
                .name("Updated Starship Monitor")
                .description("Updated description")
                .price(BigDecimal.valueOf(2500))
                .category(PRODUCT_CATEGORY)
                .stockQuantity(PRODUCT_STOCK_QUANTITY)
                .build();

        Product updatedProduct = productMapper.toProduct(updatedDto);

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Starship Monitor"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.price").value(2500));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void testGetProductById_notFound() throws Exception {
        when(productService.getProductById(999L)).thenThrow(new ProductNotFoundException(999L));

        mockMvc.perform(get("/api/v1/products/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(999L);
    }

    @Test
    void testUpdateProduct_invalidData_returnsBadRequest() throws Exception {
        ProductDetailsDto invalidDto = ProductDetailsDto.builder()
                .name("Invalid")
                .description("Desc")
                .price(BigDecimal.valueOf(10))
                .category(PRODUCT_CATEGORY)
                .stockQuantity(5)
                .build();

        mockMvc.perform(put("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("validation_error"))
                .andExpect(jsonPath("$.detail", containsString("name")));

        verify(productService, never()).updateProduct(eq(1L), any(Product.class));
    }

}
