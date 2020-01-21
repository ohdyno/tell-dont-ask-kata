package it.gabrieletondi.telldontaskkata.useCase;

public class OrderShipmentRequest {
    private int orderId;

    public OrderShipmentRequest(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }
}
