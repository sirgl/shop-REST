package ru.nsu.ccfit.rivanov.orders;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryOrderRepositoryFindByPeriodTest {
    InMemoryOrderRepository repository = new InMemoryOrderRepository();
    private Order order1 = new Order(new ArrayList<>(), new Date(10));
    private Order order2 = new Order(new ArrayList<>(), new Date(20));
    private Order order3 = new Order(new ArrayList<>(), new Date(30));
    private Order order4 = new Order(new ArrayList<>(), new Date(40));

    @Before
    public void setUp() throws Exception {
        repository.addOrder(order1);
        repository.addOrder(order2);
        repository.addOrder(order3);
        repository.addOrder(order4);
    }

    @Test
    public void testFindByPeriodBorders() {
        assertThat(repository.findOrdersByPeriod(new Date(10), new Date(30)))
                .as("Border values must not be included")
                .containsExactly(order2);
    }

    @Test
    public void testFindByPeriod() {
        assertThat(repository.findOrdersByPeriod(new Date(15), new Date(45)))
                .as("It must include values inside")
                .containsExactly(order2, order3, order4);
    }
}
