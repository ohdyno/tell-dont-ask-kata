package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderApprovalUseCase {
    private final OrderRepository orderRepository;

    public OrderApprovalUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void run(OrderApprovalRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());

        if (order.cannotBeChanged()) {
            throw new ShippedOrdersCannotBeChangedException();
        }
        
        if (request.isApproved() && order.isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }
        
        if (!request.isApproved() && order.isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        if (request.isApproved()) {
            order.approve();
        } else {
            order.reject();
        }
        
        orderRepository.save(order);
    }
}
