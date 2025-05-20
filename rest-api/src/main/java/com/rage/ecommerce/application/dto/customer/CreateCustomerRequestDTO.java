package com.rage.ecommerce.application.dto.customer;

import com.rage.ecommerce.domain.enums.CustomerSubscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequestDTO {

    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private CustomerSubscription subscription;
}
