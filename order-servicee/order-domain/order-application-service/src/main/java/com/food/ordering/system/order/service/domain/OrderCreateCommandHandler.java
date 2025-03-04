package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.CreatedOrderCommand;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderCreatedHelper orderCreatedHelper;
    private final OrderDataMapper   orderDataMapper;
    private final ApplicationDomainEventPublisher applicationDomainEventPublisher;

    public OrderCreateCommandHandler(OrderCreatedHelper orderCreatedHelper, OrderDataMapper orderDataMapper, ApplicationDomainEventPublisher applicationDomainEventPublisher) {
        this.orderCreatedHelper = orderCreatedHelper;
        this.orderDataMapper = orderDataMapper;
        this.applicationDomainEventPublisher = applicationDomainEventPublisher;
    }


    public CreateOrderResponse createOrder(CreatedOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreatedHelper.persisteOrder(createOrderCommand);
        log.info("Order with id {} has been created", orderCreatedEvent.getOrder().getId().getValue());
        CreateOrderResponse createOrderResponse = orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(), "Order created successfully");
        applicationDomainEventPublisher.publish(orderCreatedEvent);
        return createOrderResponse;
    }


}
