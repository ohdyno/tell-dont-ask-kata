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
        setStatus(OrderStatus.CREATED);
        setItems(new ArrayList<>());
        setCurrency("EUR");
        setTotal(new BigDecimal("0.00"));
        setTax(new BigDecimal("0.00"));
    }

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

            final OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setTax(taxAmount);
            orderItem.setTaxedAmount(taxedAmount);
            getItems().add(orderItem);

            setTotal(getTotal().add(taxedAmount));
            setTax(getTax().add(taxAmount));
        }
    }
}
