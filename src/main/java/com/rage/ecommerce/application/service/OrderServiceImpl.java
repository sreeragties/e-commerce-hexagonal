package com.rage.ecommerce.application.service;

import com.rage.ecommerce.domain.enums.OrderEvent;
import com.rage.ecommerce.domain.enums.OrderState;
import com.rage.ecommerce.domain.model.Order;
import com.rage.ecommerce.domain.port.in.OrderService;
import com.rage.ecommerce.domain.port.out.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final StateMachineFactory<OrderState, OrderEvent> stateMachineFactory;


    private StateMachine<OrderState, OrderEvent> getStateMachine(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        StateMachine<OrderState, OrderEvent> sm = stateMachineFactory.getStateMachine(order.getId().toString());
        sm.startReactively().block(); // Start the state machine if it's not already running
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> sma.resetStateMachine(new DefaultStateMachineContext<>(order.getOrderState(), null, null, null)));
        return sm;
    }

    private void sendEvent(StateMachine<OrderState, OrderEvent> stateMachine, OrderEvent event) {
        Message<OrderEvent> message = MessageBuilder.withPayload(event).build();
        stateMachine.sendEvent(message);
    }

    private void saveState(StateMachine<OrderState, OrderEvent> stateMachine, Order order) {
        order.setOrderState(stateMachine.getState().getId());
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order createOrder() {
        Order order = new Order();
        order.setOrderState(OrderState.CREATED);
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Optional<Order> getOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public boolean checkOffer(UUID orderId) {
        StateMachine<OrderState, OrderEvent> stateMachine = getStateMachine(orderId);
        sendEvent(stateMachine, OrderEvent.CHECK_OFFER);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        saveState(stateMachine, order);
        return stateMachine.getState().getId() == OrderState.OFFER_CHECKING;
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
}
