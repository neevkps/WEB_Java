package com.wearetrying.space_cats_market.domain;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Product {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String category;
        private Integer stockQuantity;
}

