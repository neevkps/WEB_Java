package com.wearetrying.space_cats_market.service.mapper;

import com.wearetrying.space_cats_market.dto.product.ProductDetailsDto;
import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.dto.product.ProductDetailsListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    ProductDetailsDto toProductDetailsDto(Product product);


    default ProductDetailsListDto toProductDetailsListDto(List<Product> products) {
        return ProductDetailsListDto.builder()
                .productDetailsDto(
                        products.stream()
                                .map(this::toProductDetailsDto)
                                .toList()
                )
                .build();
    }

    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    Product toProduct(ProductDetailsDto dto);

}
