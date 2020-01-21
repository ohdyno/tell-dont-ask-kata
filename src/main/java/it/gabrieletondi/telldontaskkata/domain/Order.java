package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;
import it.gabrieletondi.telldontaskkata.useCase.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

public class Order {
    private BigDecimal total;
    private String currency;
    private List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private int id;

    public Order() {
        this(OrderStatus.CREATED);
    }

    public Order(OrderStatus status) {
        this.id = 1;
        this.status = status;
        this.items = new ArrayList<>();
        this.currency = "EUR";
        this.total = new BigDecimal("0.00");
        this.tax = new BigDecimal("0.00");
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public int getId() {
        return id;
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
        this.status = OrderStatus.APPROVED;
    }

    public void reject() {
        this.status = OrderStatus.REJECTED;
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
        this.status = OrderStatus.SHIPPED;
    }

    public boolean isCreated() {
        return status.equals(OrderStatus.CREATED);
    }

    public void ship(ShipmentService shipmentService) {
        if (cannotBeShipped()) {
            throw new OrderCannotBeShippedException();
        }

        if (isShipped()) {
            throw new OrderCannotBeShippedTwiceException();
        }

        shipmentService.ship(this);

        shipped();
    }

    public void process(SellItemsRequest request, ProductCatalog productCatalog) {
        request.process(this, productCatalog);
    }

    public void sell(Product product, int quantity) {
        if (product == null) {
            throw new UnknownProductException();
        }
        else {
            final BigDecimal taxedAmount = product.calculateTaxedAmount(quantity);
            final BigDecimal taxAmount = product.calculateTaxAmount(quantity);

            final OrderItem orderItem = new OrderItem(product, quantity, taxAmount, taxedAmount);
            getItems().add(orderItem);

            this.total = getTotal().add(taxedAmount);
            this.tax = getTax().add(taxAmount);
        }
    }
}
