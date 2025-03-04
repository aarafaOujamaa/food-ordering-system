package com.food.ordering.system.order.service.dataaccess.order.mapper;

import com.domain.ordering.system.domain.valueobject.OrderId;
import com.domain.ordering.system.domain.valueobject.ProductId;
import com.domain.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .address(deliveryAddressToDeliveryAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().getAmount())
                .items(orderItemsToOrderItemEntities(order.getOrderItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() !=null ? String.join(order.FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "null")
                .build();

        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getOrderItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));

        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .OrderId(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .deliveryAddress(deliveryAddressEntityToDeliveryAddress(orderEntity.getAddress()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(orderEntity.getFailureMessages() != null ? orderEntity.getFailureMessages().split(orderEntity.FAILURE_MESSAGE_DELIMITER) : null)
                .orderItems(orderItemEntitiesToOrderItems(orderEntity.getOrderItems()))
                .price(orderEntity.getPrice())
                .trackingIds(new TrackingId(orderEntity.getTrackingId()))
                .build();
    }

    private List<OrderItem> orderItemEntitiesToOrderItems(List<OrderItemEntity> orderItems) {
        return orderItems.stream().map(orderItem ->
                OrderItem.builder()
                .orderItemId(new OrderItemId(orderItem.getId()))
                .product(new Product(new ProductId(orderItem.getProductId())))
                .quantity(orderItem.getQuantiy())
                .price(new Price(orderItem.getPrice()))
                .subTotal(new SubTotal(orderItem.getSubtotal()))
                .build()).toList();
    }

    private StreetAddress deliveryAddressEntityToDeliveryAddress(OrderAddressEntity address) {
        return new StreetAddress(address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getPostalCode());
    }


    private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> OrderItemEntity.builder()
                                .id(orderItem.getId().getValue())
                                .productId(orderItem.getProduct().getId().getValue())
                                .quantiy(orderItem.getQuantity())
                                .price(orderItem.getPrice().getAmount())
                                .subtotal(orderItem.getSubTotal().getAmount())
                                .build()).toList();
    }

    private OrderAddressEntity deliveryAddressToDeliveryAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .street(deliveryAddress.getStreet())
                .city(deliveryAddress.getCity())
                .postalCode(deliveryAddress.getPostalCode())
                .build();
    }
}
