package com.wearetrying.space_cats_market.domain;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Order {
    private Long id;
    private String trackingNumber;
    private Customer customer;
    private List<OrderItem> items;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
}
