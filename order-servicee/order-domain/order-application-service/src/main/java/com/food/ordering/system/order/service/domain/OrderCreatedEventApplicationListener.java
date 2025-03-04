package com.food.ordering.system.order.service.domain;


import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatePaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class OrderCreatedEventApplicationListener {


    private final OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher;

    public OrderCreatedEventApplicationListener(OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher) {
        this.orderCreatePaymentRequestMessagePublisher = orderCreatePaymentRequestMessagePublisher;
    }

    @TransactionalEventListener
    void process(OrderCreatedEvent orderCreatedEvent) {


    }


}
