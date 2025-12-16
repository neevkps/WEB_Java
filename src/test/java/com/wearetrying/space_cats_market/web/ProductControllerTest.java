package com.wearetrying.space_cats_market.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.dto.ProductDetailsDto;
import com.wearetrying.space_cats_market.services.ProductService;
import com.wearetrying.space_cats_market.services.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException; // Важливий імпорт
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    private ProductDetailsDto testDto;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testDto = ProductDetailsDto.builder()
                .name("Galaxy Laser Pointer")
                .description("Best toy for moon cats")
                .price(new BigDecimal("15.99"))
                .category("Toys")
                .stockQuantity(10)
                .build();

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Galaxy Laser Pointer");
    }

    @Test
    @WithMockUser(username = "astro-user", roles = "USER")
    void shouldReturnAllProducts() throws Exception {
        when(productService.findAll()).thenReturn(List.of(testProduct));
        when(productMapper.toDto(testProduct)).thenReturn(testDto);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser(username = "astro-user")
    void shouldReturnProductById() throws Exception {
        Long id = 1L;
        when(productService.findById(id)).thenReturn(testProduct);
        when(productMapper.toDto(testProduct)).thenReturn(testDto);

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Galaxy Laser Pointer"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateProduct_WhenValidInput_AndAdmin() throws Exception {
        when(productService.createProduct(any(ProductDetailsDto.class))).thenReturn(testProduct);
        when(productMapper.toDto(testProduct)).thenReturn(testDto);

        String jsonRequest = objectMapper.writeValueAsString(testDto);

        mockMvc.perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Galaxy Laser Pointer"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnBadRequest_WhenCreatingInvalidProduct() throws Exception {
        ProductDetailsDto invalidDto = ProductDetailsDto.builder()
                .name("cat")
                .price(new BigDecimal("-5"))
                .stockQuantity(1)
                .build();

        mockMvc.perform(post("/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteProduct_WhenAdmin() throws Exception {
        Long id = 1L;
        doNothing().when(productService).deleteById(id);

        mockMvc.perform(delete("/products/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "simple-user", roles = {"USER"})
    void shouldForbidDelete_WhenUserIsNotAdmin() throws Exception {
        Long id = 1L;
        doThrow(new AccessDeniedException("Access Denied"))
                .when(productService).deleteById(id);

        mockMvc.perform(delete("/products/{id}", id)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}