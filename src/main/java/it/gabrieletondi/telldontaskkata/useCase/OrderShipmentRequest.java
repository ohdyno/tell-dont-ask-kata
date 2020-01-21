package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;

public class OrderShipmentRequest {
    private final int orderId;

    public OrderShipmentRequest(int orderId) {
        this.orderId = orderId;
    }

    void process(OrderRepository orderRepository, ShipmentService shipmentService) {
        final Order order = orderRepository.getById(orderId);

        order.ship(shipmentService);

        orderRepository.save(order);
    }
}
