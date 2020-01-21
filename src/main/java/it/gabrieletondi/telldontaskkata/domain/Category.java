package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

public class Category {
    private String name;
    private BigDecimal taxPercentage;

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }
}
