package com.food.ordering.system.order.service.domain.exception;

import com.domain.ordering.system.domain.exception.DomainException;

public class OrderNotFoundException extends DomainException {


    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
