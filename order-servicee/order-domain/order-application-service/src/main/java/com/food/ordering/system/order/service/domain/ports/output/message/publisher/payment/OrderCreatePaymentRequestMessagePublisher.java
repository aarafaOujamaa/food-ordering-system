package com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import com.domain.ordering.system.domain.entity.publisher.DomainEventPublisher;
import com.domain.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatePaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {


}
