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

    public void approve(Order order) {
        if (approved) {
            order.approved();
        } else {
            order.rejected();
        }
    }

    void approve(OrderRepository orderRepository) {
        final Order order = orderRepository.getById(orderId);

        order.process(this);

        orderRepository.save(order);
    }
}
