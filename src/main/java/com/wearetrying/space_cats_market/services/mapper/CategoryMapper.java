package com.wearetrying.space_cats_market.services.mapper;
import com.wearetrying.space_cats_market.domain.Category;
import com.wearetrying.space_cats_market.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toDomain(CategoryEntity entity);

    CategoryEntity toEntity(Category domain);
}
