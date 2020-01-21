package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

public class SellItemRequest {
    private int quantity;
    private String productName;

    public SellItemRequest(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

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

        order.sell(product, getQuantity());
    }
}
