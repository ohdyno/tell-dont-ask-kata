package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.util.List;

public class SellItemsRequest {
    private final List<SellItemRequest> requests;

    public SellItemsRequest(List<SellItemRequest> requests) {
        this.requests = requests;
    }

    void process(ProductCatalog productCatalog, OrderRepository orderRepository) {
        Order order = new Order();

        for (SellItemRequest itemRequest : requests) {
            itemRequest.process(order, productCatalog);
        }

        orderRepository.save(order);
    }
}
