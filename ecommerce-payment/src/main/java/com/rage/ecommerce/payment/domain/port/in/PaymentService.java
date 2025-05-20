package com.rage.ecommerce.payment.domain.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rage.ecommerce.payment.application.dto.ProcessPaymentRequestDTO;

public interface PaymentService {
    void handleAndExecutePayment(ProcessPaymentRequestDTO dto, String key, String correlationIdHeader) throws JsonProcessingException;
}
