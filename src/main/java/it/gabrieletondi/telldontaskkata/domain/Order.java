package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;
import it.gabrieletondi.telldontaskkata.useCase.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

public class Order {
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

    public int getId() {
        return id;
    }

    public boolean isRejected() {
        return status.equals(OrderStatus.REJECTED);
    }

    public boolean isApproved() {
        return status.equals(OrderStatus.APPROVED);
    }

    public void approve() {
        if (isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        this.status = OrderStatus.APPROVED;
    }

    public void reject() {
        if (isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
        this.status = OrderStatus.REJECTED;
    }

    public void process(OrderApprovalRequest request) {
        if (isShipped()) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        request.process(this);
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
            items.add(orderItem);

            this.total = total.add(taxedAmount);
            this.tax = tax.add(taxAmount);
        }
    }
}
