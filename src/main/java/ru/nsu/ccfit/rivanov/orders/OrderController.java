package ru.nsu.ccfit.rivanov.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@PreAuthorize("hasRole('ROLE_admin')")
@RestController
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RequestMapping(value = "orders", method = RequestMethod.GET)
    public Collection<Order> getOrders(@RequestParam long fromTimestamp, @RequestParam long toTimestamp) {
        return orderRepository.findOrdersByPeriod(new Date(fromTimestamp), new Date(toTimestamp));
    }

    @RequestMapping(value = "orders", method = RequestMethod.POST)
    public void addOrder(@RequestBody Order order) {
        orderRepository.addOrder(order);
    }
}
