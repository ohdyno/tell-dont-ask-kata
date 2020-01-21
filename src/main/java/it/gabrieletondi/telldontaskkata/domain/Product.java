package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.valueOf;

public class Product {
    private final String name;
    private final BigDecimal price;
    private final Category category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name) &&
                price.equals(product.price) &&
                category.equals(product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category);
    }

    public Product(String name, BigDecimal price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public BigDecimal calculateTaxedAmount(int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity)).add(calculateTaxAmount(quantity));
    }

    public BigDecimal calculateTaxAmount(int quantity) {
        return category.calculateUnitaryTax(price).multiply(BigDecimal.valueOf(quantity));
    }

    public OrderItem order(int quantity) {
        return new OrderItem(this, quantity);
    }

    public boolean hasName(String name) {
        return this.name.equals(name);
    }
}
