package com.rage.ecommerce.drools.infrastructure.adapter.in;

import com.rage.ecommerce.drools.application.dto.ApplyOfferRequestDTO;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class KafkaMessageListener {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KafkaMessageListener.class);

    @Autowired
    private ApplyOfferRequestDTOHandler applyOfferRequestDTOHandler;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            ApplyOfferRequestDTO dto = objectMapper.readValue(record.value(), ApplyOfferRequestDTO.class);
            applyOfferRequestDTOHandler.handle(dto, record.key());
        } catch (IOException e) {
            log.error("Error processing CheckOrderResponseDTO message: {}", e.getMessage());
        }
    }

    @Component
    public static class ApplyOfferRequestDTOHandler implements MessageHandler<ApplyOfferRequestDTO> {
        @Override
        public void handle(ApplyOfferRequestDTO message, String key) {
            log.info("Handling CheckOrderResponseDTO. Key: {}, Message: {}", key, message);
        }
    }

    public interface MessageHandler<T> {
        void handle(T message, String key);
    }
}

