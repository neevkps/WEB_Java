package com.wearetrying.space_cats_market.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Customer {
    private Long id;
    private String username;
    private String email;
    private String password;
}
