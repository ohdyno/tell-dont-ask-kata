package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.*;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.useCase.creation.OrderCreationUseCase;
import it.gabrieletondi.telldontaskkata.useCase.creation.SellItemRequest;
import it.gabrieletondi.telldontaskkata.useCase.creation.SellItemsRequest;
import it.gabrieletondi.telldontaskkata.useCase.creation.UnknownProductException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderCreationUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final Category food = new Category(new BigDecimal("10"));
    private final ProductCatalog productCatalog = new InMemoryProductCatalog(
            Arrays.asList(
                    new Product("salad", new BigDecimal("3.56"), food),
                    new Product("tomato", new BigDecimal("4.65"), food)
            )
    );
    private final OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, productCatalog);

    @Test
    public void sellMultipleItems() {
        final SellItemsRequest request = new SellItemsRequest(
                Arrays.asList(
                        new SellItemRequest("salad", 2),
                        new SellItemRequest("tomato", 3)
                )
        );

        final List<OrderItem> expectedOrderItems = Arrays.asList(
                new OrderItem(productCatalog.getByName("salad"), 2),
                new OrderItem(productCatalog.getByName("tomato"), 3)
        );

        Order expectedOrder = new Order(OrderStatus.CREATED, new BigDecimal("23.20"), new BigDecimal("2.13"), expectedOrderItems, 1);

        useCase.run(request);

        assertEquals(expectedOrder, orderRepository.getSavedOrder());
    }

    @Test(expected = UnknownProductException.class)
    public void unknownProduct() {
        SellItemsRequest request = new SellItemsRequest(
                Collections.singletonList(new SellItemRequest("unknown product", 0))
        );

        useCase.run(request);
    }
}
