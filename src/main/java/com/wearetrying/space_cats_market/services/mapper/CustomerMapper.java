package com.wearetrying.space_cats_market.services.mapper;
import com.wearetrying.space_cats_market.domain.Customer;
import com.wearetrying.space_cats_market.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toDomain(CustomerEntity entity);

    CustomerEntity toEntity(Customer domain);
}
