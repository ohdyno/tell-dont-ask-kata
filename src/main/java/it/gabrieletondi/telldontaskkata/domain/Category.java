package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

public class Category {
    private final BigDecimal taxPercentage;

    public Category(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

}
