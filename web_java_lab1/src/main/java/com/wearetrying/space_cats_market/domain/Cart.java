package com.wearetrying.space_cats_market.domain;

import java.util.List;
import java.util.UUID;

import lombok.*;

@Value
@Builder(toBuilder = true)
public class Cart {
    UUID id;
    List<Product> products;
}
