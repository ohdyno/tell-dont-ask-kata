package it.gabrieletondi.telldontaskkata.domain;

import java.util.Objects;

public class OrderItem {
    private final Product product;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void addedTo(Order order) {
        order.addToTax(product.calculateTaxAmount(quantity));
        order.addToTotal(product.calculateTaxedAmount(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
                product.equals(orderItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity);
    }
}
