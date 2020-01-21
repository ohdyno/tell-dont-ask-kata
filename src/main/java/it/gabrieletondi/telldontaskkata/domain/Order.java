package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.service.ShipmentService;
import it.gabrieletondi.telldontaskkata.useCase.approval.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.useCase.approval.OrderApprovalRequest;
import it.gabrieletondi.telldontaskkata.useCase.approval.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.useCase.creation.UnknownProductException;
import it.gabrieletondi.telldontaskkata.useCase.shipment.OrderCannotBeShippedException;
import it.gabrieletondi.telldontaskkata.useCase.shipment.OrderCannotBeShippedTwiceException;
import it.gabrieletondi.telldontaskkata.useCase.shipment.ShippedOrdersCannotBeChangedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

public class Order {
    private BigDecimal total;

    private final String currency;
    private final List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private final int id;
    public Order() {
        this(OrderStatus.CREATED, 1);
    }

    public Order(OrderStatus status, int id) {
        this(status, new BigDecimal("0.00"), new BigDecimal("0.00"), new ArrayList<>(), id);
    }

    public Order(OrderStatus status, BigDecimal total, BigDecimal tax, List<OrderItem> items, int id) {
        this.status = status;
        this.total = total;
        this.tax = tax;
        this.id = id;
        this.items = items;
        this.currency = "EUR";
    }

    public void ship(ShipmentService shipmentService) {
        if (status.equals(CREATED) || status.equals(REJECTED)) {
            throw new OrderCannotBeShippedException();
        }

        if (status.equals(SHIPPED)) {
            throw new OrderCannotBeShippedTwiceException();
        }

        shipmentService.ship(this);

        this.status = OrderStatus.SHIPPED;
    }

    public void sell(Product product, int quantity) {
        if (product == null) {
            throw new UnknownProductException();
        }
        
        final OrderItem orderItem = product.order(quantity);
        orderItem.addTo(this);
    }

    public void approve(OrderApprovalRequest request) {
        if (status.equals(SHIPPED)) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        request.approve(this);
    }

    public void approved() {
        if (status.equals(OrderStatus.REJECTED)) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        this.status = OrderStatus.APPROVED;
    }

    public void rejected() {
        if (status.equals(OrderStatus.APPROVED)) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
        this.status = OrderStatus.REJECTED;
    }

    public boolean hasId(int orderId) {
        return id == orderId;
    }

    public void addToTax(BigDecimal tax) {
        this.tax = this.tax.add(tax);
    }

    public void addToTotal(BigDecimal amount) {
        this.total = total.add(amount);
    }

    public void add(OrderItem orderItem) {
        items.add(orderItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                total.equals(order.total) &&
                currency.equals(order.currency) &&
                items.equals(order.items) &&
                tax.equals(order.tax) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, currency, items, tax, status, id);
    }
}
