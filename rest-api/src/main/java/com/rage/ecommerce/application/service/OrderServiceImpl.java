package com.rage.ecommerce.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rage.ecommerce.application.dto.order.*;
import com.rage.ecommerce.application.mapper.OrderMapper;
import com.rage.ecommerce.domain.enums.OrderEvent;
import com.rage.ecommerce.domain.enums.OrderState;
import com.rage.ecommerce.domain.exception.ResourceNotFoundException;
import com.rage.ecommerce.domain.exception.ServiceException;
import com.rage.ecommerce.domain.model.Order;
import com.rage.ecommerce.domain.port.in.OrderService;
import com.rage.ecommerce.domain.port.out.repository.CustomerRepository;
import com.rage.ecommerce.domain.port.out.repository.ItemRepository;
import com.rage.ecommerce.domain.port.out.repository.OrderRepository;
import com.rage.ecommerce.infrastructure.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Value(value = "${kafka.topic.name}")
    private String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final StateMachineFactory<OrderState, OrderEvent> stateMachineFactory;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;

    private static final String ORDER_NOT_FOUND_LITERAL = "Order not found with id: ";

    @Transactional
    @Override
    public CreateOrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequestDTO) throws JsonProcessingException {
        if (createOrderRequestDTO == null) {
            throw new BadRequestException("Order request cannot be null");
        }
        Order order = orderMapper.toDomain(createOrderRequestDTO);
        if (order == null) {
            throw new ServiceException("Failed to map order request to domain object");
        }
        order.setOrderState(OrderState.CREATED);

        var response = orderRepository.save(order);
        var dtoResponse = orderMapper.toCreateOrderResponseDTO(response);
        try {
            sendProducerMessage(dtoResponse.getClass().getSimpleName(), response, response.getProcessId());
        } catch (Exception e) {
            log.error("Order created successfully but failed to send message: {}", e.getMessage());
        }
        return orderMapper.toCreateOrderResponseDTO(response);
    }

    @Transactional
    @Override
    public Optional<Order> getOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public void checkOffer(Order order) throws JsonProcessingException {
        var processId = order.getProcessId();
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(order.getProcessId());
        sendEvent(stateMachine, OrderEvent.CHECK_OFFER);

        var response = saveState(stateMachine, order);
        var dtoResponse = orderMapper.toCheckOrderResponseDTO(response);

        if (dtoResponse == null) {
            throw new ServiceException("Failed to map order request to domain object");
        }

        var customer = customerRepository.findByCustomerId(dtoResponse.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found with id in checkOffer: " + processId));
        var item = itemRepository.findByItemId(dtoResponse.getItemId()).orElseThrow(
                () -> new ResourceNotFoundException("Item not found with id in checkOffer: " + processId));

        dtoResponse.setSubscription(customer.getSubscription());
        dtoResponse.setDateOfBirth(customer.getDateOfBirth());
        dtoResponse.setItemOfferLevel(item.getItemOfferLevel());

        try {
            sendProducerMessage(dtoResponse.getClass().getSimpleName(), dtoResponse, response.getProcessId());
        } catch (Exception e) {
            log.error("Order checked successfully but failed to send message: {}", e.getMessage());
        }
    }


    @Transactional
    @Override
    public void applyOffer(Order order) throws JsonProcessingException {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(order.getProcessId());
        sendEvent(stateMachine, OrderEvent.APPLY_OFFER);
        var item = itemRepository.findByItemId(order.getItemId()).orElseThrow(
                () -> new RuntimeException("Item not found with id in checkOffer: " + order.getItemId()));
        double offerRate = order.getOfferRate();
        double calculatedPrice = item.getPrice() - item.getPrice()*offerRate/100;
        order.setCalculatedPrice(calculatedPrice);
        var response = saveState(stateMachine, order);

        var dtoResponse = orderMapper.toApplyOfferResponseDTO(response);

        sendProducerMessage(dtoResponse.getClass().getSimpleName(), response, response.getProcessId());
    }

    @Transactional
    @Override
    public boolean cancelOffer(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.CANCEL_OFFER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND_LITERAL + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.PAYMENT_PENDING;
    }

    @Transactional
    @Override
    public boolean placeOrder(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.PLACE_ORDER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND_LITERAL + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.PAYMENT_PENDING;
    }


    @Transactional
    @Override
    public void makePayment(Order order) throws JsonProcessingException {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(order.getProcessId());
        sendEvent(stateMachine, OrderEvent.MAKE_PAYMENT);
        var response = saveState(stateMachine, order);
        var dtoResponse = orderMapper.toMakePaymentResponseDTO(response);
        sendProducerMessage(dtoResponse.getClass().getSimpleName(), dtoResponse, response.getProcessId());
    }

    @Transactional
    @Override
    public boolean shipOrder(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.SHIP_ORDER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND_LITERAL + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.SHIPPED;
    }

    @Transactional
    @Override
    public boolean deliverOrder(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.DELIVER_ORDER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND_LITERAL + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.DELIVERED;
    }

    @Transactional
    @Override
    public boolean returnOrder(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.RETURN_ORDER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND_LITERAL + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.RETURNED;
    }

    @Transactional
    @Override
    public boolean cancelOrder(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.CANCEL_ORDER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND_LITERAL + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.CANCELLED;
    }

    private <T> void sendProducerMessage(String className, T message, UUID processId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String serialisedResponse = objectMapper.writeValueAsString(message);
        String serialisedProcessId = processId.toString();

        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("DTOClassName", className.getBytes()));

        ProducerRecord<String, String> producerRecord = new ProducerRecord <>(topicName, null, serialisedProcessId, serialisedResponse, headers);
        kafkaTemplate.send(producerRecord);
    }

    private StateMachine<OrderState, OrderEvent> getStateMachine(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND_LITERAL + orderId));

        StateMachine<OrderState, OrderEvent> sm = stateMachineFactory.getStateMachine(order.getProcessId().toString());
        sm.startReactively().block();

        return sm;
    }

    private void sendEvent(StateMachine<OrderState, OrderEvent> stateMachine, OrderEvent event) {
        Message<OrderEvent> message = MessageBuilder.withPayload(event).build();
        Flux<StateMachineEventResult<OrderState, OrderEvent>> resultFlux = stateMachine.sendEvent(Mono.just(message));
        resultFlux
                .doOnComplete(() -> log.debug("Event {} sent to state machine for order {}", event, stateMachine.getId()))
                .doOnError(e -> log.error("Failed to send event {} to state machine for order {}: {}", event, stateMachine.getId(), e.getMessage()))
                .subscribe();
    }

    private Order saveState(StateMachine<OrderState, OrderEvent> stateMachine, Order order) {
        order.setOrderState(stateMachine.getState().getId());
        return orderRepository.save(order);
    }
}
