package com.wearetrying.space_cats_market.services.mapper;
import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.dto.ProductDetailsDto;
import com.wearetrying.space_cats_market.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    Product toDomain(ProductEntity entity);

    ProductEntity toEntity(Product domain);

    @Mapping(source = "category.name", target = "category")
    ProductDetailsDto toDto(Product domain);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    Product toDomain(ProductDetailsDto dto);
}
