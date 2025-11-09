package com.wearetrying.space_cats_market.domain;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.*;

@Value
@Builder(toBuilder = true)
public class Order {
    UUID id;
    List<Product> products;
    BigDecimal totalPrice;
    LocalDateTime createdAt;
}
