package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderApprovalRequest {
    private int orderId;
    private boolean approved;

    public OrderApprovalRequest(int orderId, boolean approved) {
        this.orderId = orderId;
        this.approved = approved;
    }

    public void process(Order order) {
        if (approved) {
            order.approve();
        } else {
            order.reject();
        }
    }

    void process(OrderRepository orderRepository) {
        final Order order = orderRepository.getById(orderId);

        order.process(this);

        orderRepository.save(order);
    }
}
