package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

public class OrderItem {
    private Product product;
    private int quantity;
    private BigDecimal taxedAmount;
    private BigDecimal tax;

    public OrderItem(Product product, int quantity, BigDecimal tax, BigDecimal taxedAmount) {
        this.product = product;
        this.quantity = quantity;
        this.tax = tax;
        this.taxedAmount = taxedAmount;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTaxedAmount() {
        return taxedAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

}
