package com.food.ordering.system.order.service.domain.dto.create;


import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;


@Getter
@Builder
@AllArgsConstructor
public class OrderAddress {

    @NonNull
    @Max(50)
    private final String street;
    @NonNull
    @Max(10)
    private final String postalCode;
    @NonNull
    @Max(50)
    private final String city;

}
