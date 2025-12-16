package com.wearetrying.space_cats_market.web;

import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.dto.ProductDetailsDto;
import com.wearetrying.space_cats_market.projection.TopProductProjection;
import com.wearetrying.space_cats_market.services.ProductService;
import com.wearetrying.space_cats_market.services.mapper.ProductMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDetailsDto>> getAllProducts() {
        List<Product> products = productService.findAll();
        List<ProductDetailsDto> dtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsDto> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDetailsDto> createProduct(@RequestBody @Valid ProductDetailsDto request) {
        Product createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productMapper.toDto(createdProduct));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
    }
    @GetMapping("/top")
    public ResponseEntity<List<TopProductProjection>> getTopProducts() {
        return ResponseEntity.ok(productService.getTopPurchasedProducts());
    }
}