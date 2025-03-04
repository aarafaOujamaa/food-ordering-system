package com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import com.domain.ordering.system.domain.entity.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
