package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Category {
    private final BigDecimal taxPercentage;

    public Category(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public BigDecimal calculateUnitaryTax(BigDecimal price) {
        return price.divide(valueOf(100)).multiply(getTaxPercentage()).setScale(2, HALF_UP);
    }
}
