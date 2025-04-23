package com.rage.ecommerce.infrastructure.config;

import com.rage.ecommerce.domain.enums.OrderEvent;
import com.rage.ecommerce.domain.enums.OrderState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderState.CREATED)
                .states(EnumSet.allOf(OrderState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
        transitions
                // From CREATED state
                .withExternal()
                .source(OrderState.CREATED)
                .target(OrderState.OFFER_CHECKING)
                .event(OrderEvent.CHECK_OFFER)
                .and()
                .withExternal()
                .source(OrderState.CREATED)
                .target(OrderState.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
                .and()

                // From OFFER_CHECKED state
                .withExternal()
                .source(OrderState.OFFER_CHECKING)
                .target(OrderState.OFFER_APPLIED)
                .event(OrderEvent.APPLY_OFFER)
                .and()
                .withExternal()
                .source(OrderState.OFFER_CHECKING)
                .target(OrderState.PAYMENT_PENDING)
                .event(OrderEvent.CANCEL_OFFER)
                .and()

                // From OFFER_APPLIED state
                .withExternal()
                .source(OrderState.OFFER_APPLIED)
                .target(OrderState.PAYMENT_PENDING)
                .event(OrderEvent.MAKE_PAYMENT)
                .and()
                .withExternal()
                .source(OrderState.OFFER_APPLIED)
                .target(OrderState.PAYMENT_PENDING)
                .event(OrderEvent.CANCEL_OFFER)
                .and()

                // From PAYMENT_PENDING state
                .withExternal()
                .source(OrderState.PAYMENT_PENDING)
                .target(OrderState.PAYMENT_APPROVED)
                .event(OrderEvent.PAYMENT_SUCCESS)
                .and()
                .withExternal()
                .source(OrderState.PAYMENT_PENDING)
                .target(OrderState.PAYMENT_REJECTED)
                .event(OrderEvent.PAYMENT_FAILED)
                .and()
                .withExternal()
                .source(OrderState.PAYMENT_PENDING)
                .target(OrderState.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
                .and()

                // From PAID state
                .withExternal()
                .source(OrderState.PAYMENT_APPROVED)
                .target(OrderState.PROCESSING)
                .event(OrderEvent.PAYMENT_SUCCESS)
                .and()
                .withExternal()
                .source(OrderState.PAYMENT_REJECTED)
                .target(OrderState.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
                .and()

                // From PROCESSING state
                .withExternal()
                .source(OrderState.PROCESSING)
                .target(OrderState.SHIPPED)
                .event(OrderEvent.SHIP_ORDER)
                .and()
                .withExternal()
                .source(OrderState.PROCESSING)
                .target(OrderState.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
                .and()

                // From SHIPPED state
                .withExternal()
                .source(OrderState.SHIPPED)
                .target(OrderState.DELIVERED)
                .event(OrderEvent.DELIVER_ORDER)
                .and()

                // From DELIVERED state
                .withExternal()
                .source(OrderState.DELIVERED)
                .target(OrderState.RETURNED)
                .event(OrderEvent.RETURN_ORDER);
    }
}