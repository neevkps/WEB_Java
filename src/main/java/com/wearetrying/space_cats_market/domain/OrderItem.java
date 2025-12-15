package com.wearetrying.space_cats_market.domain;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderItem {
    private Long id;
    private Product product;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}