package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;

public class OrderApprovalRequest {
    private int orderId;
    private boolean approved;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }

    public void process(Order order) {
        if (isApproved()) {
            if (order.isRejected()) {
                throw new RejectedOrderCannotBeApprovedException();
            }
            order.approve();
        } else {
            if (order.isApproved()) {
                throw new ApprovedOrderCannotBeRejectedException();
            }
            order.reject();
        }
    }
}
