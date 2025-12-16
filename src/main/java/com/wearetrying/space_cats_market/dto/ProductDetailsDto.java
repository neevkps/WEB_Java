package com.wearetrying.space_cats_market.dto;
import com.wearetrying.space_cats_market.dto.validation.CosmicWordCheck;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import java.math.BigDecimal;

@Value
@Builder(toBuilder = true)
@Jacksonized
@GroupSequence({ProductDetailsDto.class})
public class ProductDetailsDto {
    @NotBlank(message = "Product name is mandatory")
    @Size(max = 150, message = "Product name cannot exceed 150 characters")
    @CosmicWordCheck
    String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    String description;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    BigDecimal price;

    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    String category;

    @NotNull(message = "Stock quantity is mandatory")
    @Min(value = 0, message = "Stock quantity must be non-negative")
    Integer stockQuantity;
}
