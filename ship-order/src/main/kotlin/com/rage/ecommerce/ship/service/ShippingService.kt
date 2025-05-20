package com.rage.ecommerce.ship.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.rage.ecommerce.ship.dto.StageForDeliverRequestDTO
import com.rage.ecommerce.ship.dto.StageForDeliverResponseDTO
import com.rage.ecommerce.ship.listener.KafkaMessageListener
import com.rage.ecommerce.ship.mapper.ShipMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class ShippingService(
    private val shipMapper: ShipMapper,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${kafka.topic.name}") private val topicName: String)    {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(KafkaMessageListener::class.java)
    }

    @Throws(JsonProcessingException::class)
    fun handleAndExecutePayment(dto: StageForDeliverRequestDTO, correlationId: String?) {
        logger.info("Handling ApplyOfferRequestDTO. Correlation ID : {}, Message: {}", correlationId, dto)
        val response: StageForDeliverResponseDTO = shipMapper.toStageForDeliverResponseDTO(dto)

        val key = response.processId?.toString();

        sendProducerMessage("order.shipped", "v1.0", response, correlationId, key.toString())
    }

    @Throws(JsonProcessingException::class)
    fun <T> sendProducerMessage(
        kafkaEventType: String,
        eventVersion: String?,
        message: T,
        correlationId: String?,
        messageKey: String
    ) {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val serialisedResponse = objectMapper.writeValueAsString(message)

        val headers: MutableList<Header> = ArrayList()
        headers.add(RecordHeader("event-type", kafkaEventType.toByteArray()))
        headers.add(RecordHeader("event-version", "v1.0".toByteArray()))
        if (correlationId != null) {
            headers.add(RecordHeader("correlation-id", correlationId.toByteArray(StandardCharsets.UTF_8)))
        } else {
            logger.warn(
                "Sending Kafka event '{}' without a correlation ID. Consider propagating one.",
                kafkaEventType
            )
        }
        val producerRecord = ProducerRecord(
            topicName,
            null,
            messageKey,
            serialisedResponse,
            headers
        )

        logger.info("producer record: {} ", producerRecord);

        kafkaTemplate.send(producerRecord)
    }
}