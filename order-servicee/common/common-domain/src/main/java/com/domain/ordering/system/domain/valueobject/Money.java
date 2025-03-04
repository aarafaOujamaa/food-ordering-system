package com.domain.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;

    public static final Money ZERO = new Money(new BigDecimal(0));

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isGreaterThanZero() {
        return this.amount !=null &&  this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money other) {
        return this.amount !=null && this.amount.compareTo(other.amount) > 0;
    }

    public  BigDecimal setScale(BigDecimal amount) {
         return amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
     }

     public Money multiply(int multiplier) {
         return new Money(setScale(this.amount.multiply(new BigDecimal(multiplier))));
     }

     public Money add(Money other) {
         return new Money(setScale(this.amount.add(other.amount)));
     }

     public Money subtract(Money other) {
         return new Money(setScale(this.amount.subtract(other.amount)));
     }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
