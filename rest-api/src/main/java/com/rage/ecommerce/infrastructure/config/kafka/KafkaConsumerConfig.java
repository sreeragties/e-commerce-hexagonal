package com.rage.ecommerce.infrastructure.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${kafka.group-id}")
    private String groupId;

    // Keep your base ConsumerFactory
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    // Helper method to create a factory with a specific DTO filter
    private ConcurrentKafkaListenerContainerFactory<String, String> createContainerFactory(String expectedDtoClassName) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRecordFilterStrategy(recordItem -> {
            String dtoClassName = getDtoClassName(recordItem.headers().toArray());
            return !expectedDtoClassName.equals(dtoClassName);
        });
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> offerEvaluationResponseContainerFactory() {
        return createContainerFactory("OfferEvaluationResponseDTO");
    }

    private String getDtoClassName(Header[] headers) {
        if (headers != null) {
            for (Header header : headers) {
                if ("DTOClassName".equals(header.key())) {
                    return new String(header.value());
                }
            }
        }
        return null;
    }
}

