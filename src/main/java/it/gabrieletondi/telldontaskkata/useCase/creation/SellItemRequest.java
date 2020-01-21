package it.gabrieletondi.telldontaskkata.useCase.creation;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

public class SellItemRequest {
    private final int quantity;
    private final String productName;

    public SellItemRequest(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    void process(Order order, ProductCatalog productCatalog) {
        Product product = productCatalog.getByName(productName);

        order.sell(product, quantity);
    }
}
