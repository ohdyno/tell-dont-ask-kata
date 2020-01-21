package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {
    private final Product product;
    private final int quantity;
    private final BigDecimal taxedAmount;
    private final BigDecimal tax;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
                product.equals(orderItem.product) &&
                taxedAmount.equals(orderItem.taxedAmount) &&
                tax.equals(orderItem.tax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, taxedAmount, tax);
    }

    public OrderItem(Product product, int quantity, BigDecimal tax, BigDecimal taxedAmount) {
        this.product = product;
        this.quantity = quantity;
        this.tax = tax;
        this.taxedAmount = taxedAmount;
    }

}
