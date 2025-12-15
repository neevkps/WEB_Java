package com.wearetrying.space_cats_market.domain;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Product {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Category category;
        private Integer stockQuantity;
}

