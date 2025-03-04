package com.food.ordering.system.order.service.domain.entity;

import com.domain.ordering.system.domain.entity.AggreateRoot;
import com.domain.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggreateRoot<OrderId> {
    private final CustemerId customerId;
    private final RestaurantId restaurantId;
    private final List<OrderItem> orderItems;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items = null;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    public void intializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDIND;
        intializeOrderitems();

    }

    public void validateOrder() {
        validateInitialOrdder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if(orderStatus != OrderStatus.PENDIND) {
            throw new OrderDomainException("Order is not correct state for pay operation");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if(orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not correct state for approve operation");
        }
        orderStatus = OrderStatus.APPROED;
    }

    public void initCancel(List<String> failureMessages) {
        if(orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not correct state for initCancel operation!");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if(this.failureMessages != null || failureMessages !=null) {
            this.failureMessages.addAll(failureMessages.stream().filter(
                    message -> !message.isEmpty()).toList());
        }

        if(this.failureMessages ==null ) {
            this.failureMessages = failureMessages;
        }
    }

    public void cancel() {
        if(!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDIND)) {
            throw new OrderDomainException("Order is not correct state for finalCancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
    }

    private void validateInitialOrdder() {
        if (orderStatus != null && getId() != null) {
            throw new OrderDomainException("Order is not correct state for initialization");
        }
    }

    public void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero");
        }
    }

    private void intializeOrderitems() {
        long itemId = 1;
        for (OrderItem orderItem : items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    private void validateItemsPrice() {
        long itemId = 1;
        Money orderItemsTotal = orderItems.stream().map(orderItem -> {
            validateItemsPrice();
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if(!price.equals(orderItemsTotal))  {
            throw new OrderDomainException("Total price :" + price.getAmount() +
                    " is not equal to order items total " + orderItemsTotal.getAmount() +"!");
        }
    }

    public void validateItemsPrice(OrderItem orderItem) {
     if(!orderItem.isPriceValid()) {
         throw new OrderDomainException("Order item price" + orderItem.getPrice().getAmount() +"is not valid for" +
                 "product " + orderItem.getProduct().getId().getValue());
     }
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        orderItems = builder.orderItems;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }


    public CustemerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustemerId customerId;
        private RestaurantId restaurantId;
        private List<OrderItem> orderItems;
        private StreetAddress deliveryAddress;
        private Money price;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        public Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(OrderId orderId) {
            orderId = orderId;
            return this;
        }

        public Builder customerId(CustemerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder orderItems(List<OrderItem> val) {
            orderItems = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
