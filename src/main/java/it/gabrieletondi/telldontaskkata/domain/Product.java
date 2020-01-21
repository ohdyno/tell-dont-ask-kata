package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Product {
    private String name;
    private BigDecimal price;
    private Category category;

    public Product(String name, BigDecimal price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public BigDecimal calculateUnitaryTax() {
        return getPrice().divide(valueOf(100)).multiply(getCategory().getTaxPercentage()).setScale(2, HALF_UP);
    }

    public BigDecimal calculateTaxedAmount(int quantity) {
        return getPrice().add(calculateUnitaryTax()).setScale(2, HALF_UP).multiply(BigDecimal.valueOf(quantity)).setScale(2, HALF_UP);
    }

    public BigDecimal calculateTaxAmount(int quantity) {
        return calculateUnitaryTax().multiply(BigDecimal.valueOf(quantity));
    }
}
