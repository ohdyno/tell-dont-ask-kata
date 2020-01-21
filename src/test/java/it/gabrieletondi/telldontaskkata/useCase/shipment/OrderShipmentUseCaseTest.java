package it.gabrieletondi.telldontaskkata.useCase.shipment;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.doubles.TestShipmentService;
import it.gabrieletondi.telldontaskkata.useCase.shipment.OrderCannotBeShippedException;
import it.gabrieletondi.telldontaskkata.useCase.shipment.OrderCannotBeShippedTwiceException;
import it.gabrieletondi.telldontaskkata.useCase.shipment.OrderShipmentRequest;
import it.gabrieletondi.telldontaskkata.useCase.shipment.OrderShipmentUseCase;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class OrderShipmentUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final TestShipmentService shipmentService = new TestShipmentService();
    private final OrderShipmentUseCase useCase = new OrderShipmentUseCase(orderRepository, shipmentService);

    @Test
    public void shipApprovedOrder() {
        Order initialOrder = new Order(OrderStatus.APPROVED, 1);
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(1);

        useCase.run(request);

        assertEquals(new Order(OrderStatus.SHIPPED, 1), orderRepository.getSavedOrder());
        assertThat(shipmentService.getShippedOrder(), is(initialOrder));
    }

    @Test(expected = OrderCannotBeShippedException.class)
    public void createdOrdersCannotBeShipped() {
        Order initialOrder = new Order(OrderStatus.CREATED, 1);
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(1);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
        assertThat(shipmentService.getShippedOrder(), is(nullValue()));
    }

    @Test(expected = OrderCannotBeShippedException.class)
    public void rejectedOrdersCannotBeShipped() {
        Order initialOrder = new Order(OrderStatus.REJECTED, 1);
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(1);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
        assertThat(shipmentService.getShippedOrder(), is(nullValue()));
    }

    @Test(expected = OrderCannotBeShippedTwiceException.class)
    public void shippedOrdersCannotBeShippedAgain() {
        Order initialOrder = new Order(OrderStatus.SHIPPED, 1);
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest(1);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
        assertThat(shipmentService.getShippedOrder(), is(nullValue()));
    }
}
