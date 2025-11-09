package com.wearetrying.space_cats_market.controllers;

import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.dto.product.ProductDetailsDto;
import com.wearetrying.space_cats_market.dto.product.ProductDetailsListDto;
import com.wearetrying.space_cats_market.dto.product.ProductDetailsEntry;
import com.wearetrying.space_cats_market.service.ProductService;
import com.wearetrying.space_cats_market.service.mapper.ProductMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<ProductDetailsListDto> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        ProductDetailsListDto dtoList = productMapper.toProductDetailsListDto(products);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dtoList);
    }

    @GetMapping("/entries")
    public ResponseEntity<List<ProductDetailsEntry>> getAllProductEntries() {
        List<Product> products = productService.getAllProducts();
        List<ProductDetailsEntry> entries = productMapper.toProductDetailsEntry(products);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsDto> getProductById(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        ProductDetailsDto dto = productMapper.toProductDetailsDto(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}