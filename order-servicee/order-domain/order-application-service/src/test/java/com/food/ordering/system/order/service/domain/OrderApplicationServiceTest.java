package com.food.ordering.system.order.service.domain;


import com.domain.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.CreatedOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository CustomerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreatedOrderCommand createdOrderCommand;
    private CreatedOrderCommand createdOrderCommandWrongPrice;
    private CreatedOrderCommand createdOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("c6a3d4b0-1b4d-4b7b-8b6a-7b4d1b6a4d8b");
    private final UUID RESTAURANT_ID = UUID.fromString("c6a3d4b0-1b4d-4b7b-8b6a-7b4d1b6a4d8c");
    private final UUID ORDER_ID = UUID.fromString("c6a3d4b0-1b4d-4b7b-8b6a-7b4d1b6a4d8d");
    private final UUID PRODUCT_ID = UUID.fromString("c6a3d4b0-1b4d-4b7b-8b6a-7b4d1b6a4d8e");
    private final BigDecimal PRICE = new BigDecimal("200.00");
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderDomainService orderDomainService;

    @BeforeAll
        public void init() {
            createdOrderCommand = createdOrderCommand.builder()
                    .customerId(CUSTOMER_ID)
                    .restaurantId(RESTAURANT_ID)
                    .address(OrderAddress.builder()
                            .street("street_1")
                            .postalCode("1000AB")
                            .city("Paris")
                            .build())
                    .price(PRICE)
                    .items(List.of(OrderItem.builder()
                                    .productId(PRODUCT_ID)
                                    .quantity(1)
                                    .price(new BigDecimal(50.00))
                                    .subtotal(new BigDecimal(50.00))
                                    .build(),

                            OrderItem.builder()
                                    .productId(PRODUCT_ID)
                                    .quantity(3)
                                    .price(new BigDecimal(50.00))
                                    .subtotal(new BigDecimal(150.00))
                                    .build()))
                    .build();

            createdOrderCommandWrongPrice = createdOrderCommandWrongPrice.builder()
                    .customerId(CUSTOMER_ID)
                    .restaurantId(RESTAURANT_ID)
                    .address(OrderAddress.builder()
                            .street("street_1")
                            .postalCode("1000AB")
                            .city("Paris")
                            .build())
                    .price(PRICE)
                    .items(List.of(OrderItem.builder()
                                    .productId(PRODUCT_ID)
                                    .quantity(1)
                                    .price(new BigDecimal(80.00))
                                    .subtotal(new BigDecimal(250.00))
                                    .build(),

                            OrderItem.builder()
                                    .productId(PRODUCT_ID)
                                    .quantity(3)
                                    .price(new BigDecimal(50.00))
                                    .subtotal(new BigDecimal(150.00))
                                    .build()))
                    .build();

            createdOrderCommandWrongProductPrice = createdOrderCommandWrongProductPrice.builder()
                    .customerId(CUSTOMER_ID)
                    .restaurantId(RESTAURANT_ID)
                    .address(OrderAddress.builder()
                            .street("street_1")
                            .postalCode("1000AB")
                            .city("Paris")
                            .build())
                    .price(PRICE)
                    .items(List.of(OrderItem.builder()
                                    .productId(PRODUCT_ID)
                                    .quantity(1)
                                    .price(new BigDecimal(60.00))
                                    .subtotal(new BigDecimal(60.00))
                                    .build(),

                            OrderItem.builder()
                                    .productId(PRODUCT_ID)
                                    .quantity(3)
                                    .price(new BigDecimal(50.00))
                                    .subtotal(new BigDecimal(150.00))
                                    .build()))
                    .build();

            OrderItem.builder()
                    .productId(PRODUCT_ID)
                    .quantity(3)
                    .price(new BigDecimal(50.00))
                    .subtotal(new BigDecimal(50.00))
                    .build();

            Customer customer = new Customer();
            customer.setId(new CustomerId(CUSTOMER_ID));

            Restaurant restaurantResponse = new Restaurant.Builder()
                    .restaurantId(new RestaurantId(createdOrderCommand.getRestaurantId()))
                    .products(List.of(
                            new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                            new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))
                    ))
                    .active(true)
                    .build();

            Order order = orderDataMapper.createOrderCommandToOrder(createdOrderCommand);
            order.setId(new OrderId(ORDER_ID));

            Mockito.when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
            Mockito.when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createdOrderCommand)))
                    .thenReturn(Optional.of(restaurantResponse));
            Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        }


        @Test
        public void testCreateOrder() {
           CreateOrderResponse createOrderResponse = orderApplicationService.createOIrder(createdOrderCommand);
            Assertions.assertEquals(createOrderResponse.getOrderStatus(), OrderStatus.PENDING);
           Assertions.assertEquals(createOrderResponse.getMessgae(), "Order created successfully");
           Assertions.assertNotNull(createOrderResponse.getOrderTrackingId());
        }

        public void testCreateOrderWithWrongTotalPrice() {
            OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                    () -> orderApplicationService.
                            createOIrder(createdOrderCommandWrongPrice));

            Assertions.assertEquals("Total price : 250.00 is not equal to order items total: 200.00!", orderDomainException.getMessage());
        }

        @Test
        public void testCreateOrderWithWrongProductPrice() {
            OrderDomainException orderDomainException = Assertions.assertThrows(OrderDomainException.class,
                    () -> orderApplicationService.
                            createOIrder(createdOrderCommandWrongProductPrice));

            Assertions.assertEquals("Total price : 250.00 is not equal to order items total: 200.00!", orderDomainException.getMessage());
        }

    @Test
    public void testCreateOrderWithPassiveRestaurant() {
        Restaurant restaurantResponse = new Restaurant.Builder()
                .restaurantId(new RestaurantId(createdOrderCommand.getRestaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))
                ))
                .active(false)
                .build();

        Mockito.when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createdOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));

        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () -> orderApplicationService.createOIrder(createdOrderCommand));
        assertEquals("Restaurant with id " + RESTAURANT_ID + "is currently not active!", orderDomainException.getMessage());

    }



}
