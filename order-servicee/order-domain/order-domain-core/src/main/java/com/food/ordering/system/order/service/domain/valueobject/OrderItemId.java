package com.food.ordering.system.order.service.domain.valueobject;

import com.domain.ordering.system.domain.valueobject.BaseId;
import com.domain.ordering.system.domain.valueobject.OrderId;

public class OrderItemId  extends BaseId<Long> {

     public OrderItemId(Long value) {
        super(value);
    }
}
