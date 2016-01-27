package ru.nsu.ccfit.rivanov.orders;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderControllerTest {
    @Autowired
    private OrderController orderController = new OrderController();

    @Test
    public void testGetOrders() throws Exception {
        OrderRepository repository = mock(OrderRepository.class);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(new ArrayList<>(), new Date(99)));
        when(repository.findOrdersByPeriod(null, null))
                .thenReturn(orders);
        orderController.setOrderRepository(repository);

        orderController.getOrders(10, 20);
        verify(repository, times(1)).findOrdersByPeriod(new Date(10), new Date(20));
    }

    @Test
    public void testAddOrder() throws Exception {
        OrderRepository repository = mock(OrderRepository.class);
        Order order = new Order(new ArrayList<>(), new Date(99));
//        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        orderController.setOrderRepository(repository);

        orderController.addOrder(order);
        verify(repository, times(1)).addOrder(order);
    }
}