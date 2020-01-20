package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.useCase.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.useCase.OrderApprovalRequest;
import it.gabrieletondi.telldontaskkata.useCase.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.useCase.ShippedOrdersCannotBeChangedException;

import java.math.BigDecimal;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;
import static org.hamcrest.Matchers.is;

public class Order {
    private BigDecimal total;
    private String currency;
    private List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private int id;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean cannotBeChanged() {
        return status.equals(OrderStatus.SHIPPED);
    }

    public boolean isRejected() {
        return status.equals(OrderStatus.REJECTED);
    }

    public boolean isApproved() {
        return status.equals(OrderStatus.APPROVED);
    }

    public void approve() {
        setStatus(OrderStatus.APPROVED);
    }

    public void reject() {
        setStatus(OrderStatus.REJECTED);
    }

    public void process(OrderApprovalRequest request) {
        if (cannotBeChanged()) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (request.isApproved() && isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!request.isApproved() && isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        if (request.isApproved()) {
            approve();
        } else {
            reject();
        }
    }

    public boolean cannotBeShipped() {
        return status.equals(CREATED) || status.equals(REJECTED);
    }

    public boolean isShipped() {
        return status.equals(SHIPPED);
    }

    public void shipped() {
        setStatus(OrderStatus.SHIPPED);
    }

    public boolean isCreated() {
        return status.equals(OrderStatus.CREATED);
    }
}
