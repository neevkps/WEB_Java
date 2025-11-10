package com.wearetrying.space_cats_market.dto.product;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ProductDetailsEntry {
    Long id;
    String name;
    String description;
    BigDecimal price;
    String category;
    Integer stockQuantity;
}
