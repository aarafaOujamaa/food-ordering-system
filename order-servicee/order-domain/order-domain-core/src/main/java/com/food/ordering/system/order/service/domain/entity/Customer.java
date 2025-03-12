package com.food.ordering.system.order.service.domain.entity;

import com.domain.ordering.system.domain.entity.AggregateRoot;
import com.domain.ordering.system.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    public Customer() {
    }

    public Customer(CustomerId id) {
        super.setId(id);
    }
}
