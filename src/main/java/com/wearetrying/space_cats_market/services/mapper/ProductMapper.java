package com.wearetrying.space_cats_market.services.mapper;
import com.wearetrying.space_cats_market.domain.Product;
import com.wearetrying.space_cats_market.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    Product toDomain(ProductEntity entity);

    ProductEntity toEntity(Product domain);
}
