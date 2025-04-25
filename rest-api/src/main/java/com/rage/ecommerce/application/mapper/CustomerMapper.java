package com.rage.ecommerce.application.mapper;

import com.rage.ecommerce.application.dto.customer.CreateCustomerRequestDTO;
import com.rage.ecommerce.application.dto.customer.CreateCustomerResponseDTO;
import com.rage.ecommerce.domain.model.Customer;
import com.rage.ecommerce.infrastructure.adapter.out.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerEntity toEntity(Customer customer);
    Customer toDomain(CustomerEntity entity);

    Customer toDomain(CreateCustomerRequestDTO dto);

    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "name", target = "name")
    CreateCustomerResponseDTO toCreateCustomerResponseDTO(Customer customer);
}

