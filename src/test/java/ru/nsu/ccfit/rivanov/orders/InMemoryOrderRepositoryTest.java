package ru.nsu.ccfit.rivanov.orders;

import org.junit.Test;
import ru.nsu.ccfit.rivanov.products.Product;
import ru.nsu.ccfit.rivanov.products.ProductRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InMemoryOrderRepositoryTest {
    InMemoryOrderRepository repository = new InMemoryOrderRepository();

    @Test
    public void testAddOrder() throws Exception {
        Order order = new Order(new ArrayList<>());
        repository.addOrder(order);

        assertThat(repository.findAllOrders())
                .as("Repository must contains added order")
                .containsExactly(order);
    }

    @Test
    public void testFindAllOrders() {
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.findProductById(0)).thenReturn(Optional.of(new Product()));
        repository.setProductRepository(productRepository);

        ArrayList<OrderLine> orderLines1 = new ArrayList<>();
        orderLines1.add(new OrderLine(0, 12));
        Order order1 = new Order(orderLines1);
        repository.addOrder(order1);

        ArrayList<OrderLine> orderLines2 = new ArrayList<>();
        orderLines2.add(new OrderLine(0, 43));
        Order order2 = new Order(orderLines2, new Date(123123));
        repository.addOrder(order2);

        assertThat(repository.findAllOrders())
                .as("Repository must contains all added orders and nothing more")
                .containsExactly(order1, order2);
    }
}