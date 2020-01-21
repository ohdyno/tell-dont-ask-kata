package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.*;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

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

        useCase.run(request);

        final Order insertedOrder = orderRepository.getSavedOrder();

        List<OrderItem> expectedOrderItems = new ArrayList<>();
        expectedOrderItems.add(
                new OrderItem(
                        new Product("salad", new BigDecimal("3.56"), food),
                        2,
                        new BigDecimal("0.72"),
                        new BigDecimal("7.84")
                )
        );

        expectedOrderItems.add(
                new OrderItem(
                        new Product("tomato", new BigDecimal("4.65"), food),
                        3,
                        new BigDecimal("1.41"),
                        new BigDecimal("15.36")
                )
        );
        
        Order expectedOrder = new Order(OrderStatus.CREATED, new BigDecimal("23.20"), new BigDecimal("2.13"), expectedOrderItems, 1);
        
        assertEquals(expectedOrder, insertedOrder);
    }

    @Test(expected = UnknownProductException.class)
    public void unknownProduct() {
        SellItemsRequest request = new SellItemsRequest(
                Collections.singletonList(new SellItemRequest("unknown product", 0))
        );

        useCase.run(request);
    }
}
