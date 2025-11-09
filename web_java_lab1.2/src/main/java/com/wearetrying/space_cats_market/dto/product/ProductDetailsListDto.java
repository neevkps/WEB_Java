package com.wearetrying.space_cats_market.dto.product;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import java.util.List;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ProductDetailsListDto {
    List<ProductDetailsDto> productDetailsDto;
}
