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
    fun handleAndExecutePayment(dto: StageForDeliverRequestDTO, key: String?) {
        logger.info("Handling ApplyOfferRequestDTO. Key: {}, Message: {}", key, dto)
        val response: StageForDeliverResponseDTO? = shipMapper?.toStageForDeliverResponseDTO(dto)

        if (response != null) {
            sendProducerMessage(response.javaClass.getSimpleName(), response, response.processId)
        }
    }

    @Throws(JsonProcessingException::class)
    private fun <T> sendProducerMessage(className: String, message: T, processId: UUID?) {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val serialisedResponse = objectMapper.writeValueAsString(message)
        val serialisedProcessId = processId.toString()

        val headers: MutableList<Header> = ArrayList()
        headers.add(RecordHeader("DTOClassName", className.toByteArray()))

        val record = ProducerRecord(topicName, null, serialisedProcessId, serialisedResponse, headers)
        kafkaTemplate?.send(record)
    }
}