package com.rage.ecommerce.infrastructure.config.kafka;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${kafka.group-id.create-order}")
    private String createOrderGroupId;

    @Value("${kafka.group-id.evaluate-offer}")
    private String evaluateOfferGroupId;

    @Value("${kafka.group-id.apply-offer}")
    private String applyOfferGroupId;

    @Value("${kafka.group-id.process-payment}")
    private String processPaymentGroupId;

    private Map<String, Object> commonProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> createOrderConsumerFactory() {
        Map<String, Object> props = commonProps();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, createOrderGroupId);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, String> offerConsumerFactory() {
        Map<String, Object> props = commonProps();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, evaluateOfferGroupId);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, String> applyConsumerFactory() {
        Map<String, Object> props = commonProps();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, applyOfferGroupId);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, String> processPaymentConsumerFactory() {
        Map<String, Object> props = commonProps();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, processPaymentGroupId);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    private ConcurrentKafkaListenerContainerFactory<String, String> createContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            String expectedDtoClassName) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        factory.setRecordFilterStrategy(recordItem -> {
            String dtoClassName = getDtoClassName(recordItem.headers().toArray());
            return !expectedDtoClassName.equals(dtoClassName);
        });
        return factory;
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

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> createOrderResponseContainerFactory() {
        return createContainerFactory(createOrderConsumerFactory(), "CreateOrderResponseDTO");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> offerEvaluationResponseContainerFactory() {
        return createContainerFactory(offerConsumerFactory(), "OfferEvaluationResponseDTO");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> applyOfferResponseContainerFactory() {
        return createContainerFactory(applyConsumerFactory(), "ApplyOfferResponseDTO");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> processPaymentResponseContainerFactory() {
        return createContainerFactory(processPaymentConsumerFactory(), "ProcessPaymentResponseDTO");
    }
}

