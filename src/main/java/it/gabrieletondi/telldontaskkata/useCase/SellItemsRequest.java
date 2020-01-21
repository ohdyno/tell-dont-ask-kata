package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.util.ArrayList;
import java.util.List;

public class SellItemsRequest {
    private List<SellItemRequest> requests;

    public SellItemsRequest() {
        requests = new ArrayList<>();
    }

    public List<SellItemRequest> getRequests() {
        return requests;
    }

    public void process(Order order, ProductCatalog productCatalog) {
        for (SellItemRequest itemRequest : getRequests()) {
            itemRequest.process(order, productCatalog);
        }

    }

}
