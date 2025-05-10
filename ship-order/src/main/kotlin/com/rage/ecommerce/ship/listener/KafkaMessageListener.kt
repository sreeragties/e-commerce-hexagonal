package com.rage.ecommerce.ship.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.rage.ecommerce.ship.dto.StageForDeliverRequestDTO
import com.rage.ecommerce.ship.dto.StageForDeliverResponseDTO
import com.rage.ecommerce.ship.mapper.ShipMapper
import com.rage.ecommerce.ship.service.ShippingService
import lombok.extern.slf4j.Slf4j
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.io.IOException

@Component
@Slf4j
class KafkaMessageListener(private val shippingService: ShippingService) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(KafkaMessageListener::class.java)
    }

    @KafkaListener(topics = ["\${kafka.topic.name}"], groupId = "\${kafka.group-id}")
    fun listen(record: ConsumerRecord<String?, String?>) {
        try {
            val objectMapper = ObjectMapper()
            objectMapper.registerModule(JavaTimeModule())
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            val requestDto: StageForDeliverRequestDTO =
                objectMapper.readValue(
                    record.value(),
                    StageForDeliverRequestDTO::class.java
                )
            shippingService?.handleAndExecutePayment(requestDto, record.key())
        } catch (e: IOException) {
            logger.error(
                "Error processing CheckOrderResponseDTO message: {}",
                e.message
            )
        }
    }
}
