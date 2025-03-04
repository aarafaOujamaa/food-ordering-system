package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreatedOrderCommand;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Component
public class OrderCreatedHelper {

    private final OrderDomainService orderDomainService;

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final RestaurantRepository restaurantRepository;

    private final OrderDataMapper orderDataMapper;


    public OrderCreatedHelper(OrderDomainService orderDomainService, OrderRepository orderRepository, CustomerRepository customerRepository,
                              RestaurantRepository restaurantRepository, OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreatedEvent persisteOrder(CreatedOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent  = orderDomainService.validateAndInitiateOrder(order, restaurant);
        saveOrder(order);
        log.info("Order with id {} has been created", order.getId().getValue());
        return orderCreatedEvent;
    }


    private void checkCustomer(@NonNull UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("could not find customer with customer id {}", customerId);
            throw new IllegalStateException("Customer with id " + customerId + " does not exist");
        }
    }

    private Restaurant checkRestaurant(CreatedOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> foundRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if(foundRestaurant.isEmpty()) {
            log.warn("could not find restaurant with id {}", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Restaurant with id " + createOrderCommand.getRestaurantId() + " does not exist");
        }
        return foundRestaurant.get();

    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if(orderResult == null) {
            log.error("Order could not be saved");
            throw new OrderDomainException("Order could not be saved");
        }
        log.info("Order with id {} has been saved", orderResult.getId().getValue());
        return orderResult;

    }

}
