package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.math.BigDecimal;

public class SellItemRequest {
    private int quantity;
    private String productName;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    void process(Order order, ProductCatalog productCatalog) {
        Product product = productCatalog.getByName(getProductName());

        if (product == null) {
            throw new UnknownProductException();
        }
        else {
            final BigDecimal taxedAmount = product.calculateTaxedAmount(this);
            final BigDecimal taxAmount = product.calculateTaxAmount(this);

            final OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(getQuantity());
            orderItem.setTax(taxAmount);
            orderItem.setTaxedAmount(taxedAmount);
            order.getItems().add(orderItem);

            order.setTotal(order.getTotal().add(taxedAmount));
            order.setTax(order.getTax().add(taxAmount));
        }
    }
}
