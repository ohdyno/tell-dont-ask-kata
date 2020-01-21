package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;

public class OrderApprovalRequest {
    private int orderId;
    private boolean approved;

    public OrderApprovalRequest(int orderId, boolean approved) {
        this.orderId = orderId;
        this.approved = approved;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void process(Order order) {
        if (approved) {
            order.approve();
        } else {
            order.reject();
        }
    }
}
