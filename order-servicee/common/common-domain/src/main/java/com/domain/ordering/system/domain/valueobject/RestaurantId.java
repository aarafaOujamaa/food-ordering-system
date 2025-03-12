package com.domain.ordering.system.domain.valueobject;

import java.util.UUID;

public class RestaurantId  extends BaseId<UUID> {

    public RestaurantId(UUID value) {
        super(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantId that = (RestaurantId) o;
        return this.getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }
}