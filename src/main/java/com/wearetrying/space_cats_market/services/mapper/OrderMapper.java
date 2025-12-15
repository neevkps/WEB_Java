package com.wearetrying.space_cats_market.services.mapper;

import com.wearetrying.space_cats_market.domain.Order;
import com.wearetrying.space_cats_market.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, OrderItemMapper.class})
public interface OrderMapper {

    Order toDomain(OrderEntity entity);

    OrderEntity toEntity(Order domain);
}
