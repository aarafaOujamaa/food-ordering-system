package com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.domain.ordering.system.domain.entity.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
