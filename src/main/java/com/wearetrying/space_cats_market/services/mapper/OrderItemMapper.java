package com.wearetrying.space_cats_market.services.mapper;
import com.wearetrying.space_cats_market.domain.OrderItem;
import com.wearetrying.space_cats_market.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {

    OrderItem toDomain(OrderItemEntity entity);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "id", ignore = true)
    OrderItemEntity toEntity(OrderItem domain);
}