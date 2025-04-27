package com.rage.ecommerce.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rage.ecommerce.application.dto.order.CheckOrderResponseDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderRequestDTO;
import com.rage.ecommerce.application.dto.order.CreateOrderResponseDTO;
import com.rage.ecommerce.application.mapper.OrderMapper;
import com.rage.ecommerce.domain.enums.OrderEvent;
import com.rage.ecommerce.domain.enums.OrderState;
import com.rage.ecommerce.domain.model.Order;
import com.rage.ecommerce.domain.port.in.OrderService;
import com.rage.ecommerce.domain.port.out.repository.CustomerRepository;
import com.rage.ecommerce.domain.port.out.repository.ItemRepository;
import com.rage.ecommerce.domain.port.out.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Value(value = "${kafka.topic.name}")
    private String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final StateMachineFactory<OrderState, OrderEvent> stateMachineFactory;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public CreateOrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequestDTO) throws JsonProcessingException {
        Order order = orderMapper.toDomain(createOrderRequestDTO);
        order.setOrderState(OrderState.CREATED);

        var response = orderRepository.save(order);
        var dtoResponse = orderMapper.toCreateOrderResponseDTO(response);

        sendProducerMessage(dtoResponse.getClass().getSimpleName(), response, response.getProcessId());
        return orderMapper.toCreateOrderResponseDTO(response);
    }

    @Transactional
    @Override
    public Optional<Order> getOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public CheckOrderResponseDTO checkOffer(UUID orderId) throws JsonProcessingException {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.CHECK_OFFER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        var response = saveState(stateMachine, order);
        var dtoResponse = orderMapper.toCheckOrderResponseDTO(response);
        var customer = customerRepository.findByCustomerId(dtoResponse.getCustomerId()).orElseThrow(
                () -> new RuntimeException("Customer not found with id in checkOffer: " + orderId));
        var item = itemRepository.findByItemId(dtoResponse.getItemId()).orElseThrow(
                () -> new RuntimeException("Item not found with id in checkOffer: " + orderId));

        dtoResponse.setSubscription(customer.getSubscription());
        dtoResponse.setDateOfBirth(customer.getDateOfBirth());
        dtoResponse.setItemOfferLevel(item.getItemOfferLevel());

        sendProducerMessage(dtoResponse.getClass().getSimpleName(), dtoResponse, response.getProcessId());
        return dtoResponse;
    }


    @Transactional
    @Override
    public boolean applyOffer(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.APPLY_OFFER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.OFFER_APPLIED;
    }

    @Transactional
    @Override
    public boolean cancelOffer(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.CANCEL_OFFER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.PAYMENT_PENDING;
    }

    @Transactional
    @Override
    public boolean placeOrder(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.PLACE_ORDER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.PAYMENT_PENDING;
    }


    @Transactional
    @Override
    public boolean makePayment(UUID orderId, boolean paymentSuccessful) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.MAKE_PAYMENT);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        if (paymentSuccessful) {
            sendEvent(stateMachine, OrderEvent.PAYMENT_SUCCESS);
        }
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.PAYMENT_APPROVED || stateMachine.getState().getId() == OrderState.PAYMENT_REJECTED;
    }

    @Transactional
    @Override
    public boolean shipOrder(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.SHIP_ORDER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.SHIPPED;
    }

    @Transactional
    @Override
    public boolean deliverOrder(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.DELIVER_ORDER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.DELIVERED;
    }

    @Transactional
    @Override
    public boolean returnOrder(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.RETURN_ORDER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.RETURNED;
    }

    @Transactional
    @Override
    public boolean cancelOrder(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.CANCEL_ORDER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
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

        ProducerRecord<String, String> record = new ProducerRecord <>(topicName, null, serialisedProcessId, serialisedResponse, headers);
        kafkaTemplate.send(record);
    }

    private StateMachine<OrderState, OrderEvent> getStateMachine(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        StateMachine<OrderState, OrderEvent> sm = stateMachineFactory.getStateMachine(order.getProcessId().toString());
        sm.startReactively().block(); // Start the state machine if it's not already running
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> sma.resetStateMachine(new DefaultStateMachineContext<>(order.getOrderState(), null, null, null)));
        return sm;
    }

    private void sendEvent(StateMachine<OrderState, OrderEvent> stateMachine, OrderEvent event) {
        Message<OrderEvent> message = MessageBuilder.withPayload(event).build();
        stateMachine.sendEvent(message);
    }

    private Order saveState(StateMachine<OrderState, OrderEvent> stateMachine, Order order) {
        order.setOrderState(stateMachine.getState().getId());
        return orderRepository.save(order);
    }
}
