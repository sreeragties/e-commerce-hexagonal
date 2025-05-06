package com.rage.ecommerce.payment.application.mapper;

import com.rage.ecommerce.payment.application.dto.MakePaymentResponseDTO;
import com.rage.ecommerce.payment.application.dto.ProcessPaymentRequestDTO;
import com.rage.ecommerce.payment.application.dto.ProcessPaymentResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    ProcessPaymentRequestDTO toProcessPaymentRequestDTO(MakePaymentResponseDTO dto);

    MakePaymentResponseDTO toMakePaymentResponseDTO(ProcessPaymentRequestDTO dto);

    ProcessPaymentResponseDTO toProcessPaymentResponseDTO(ProcessPaymentRequestDTO dto);
}
