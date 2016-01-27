package ru.nsu.ccfit.rivanov.orders;

import com.sun.istack.internal.NotNull;

import java.util.Collection;
import java.util.Date;

public interface OrderRepository {
    void addOrder(@NotNull Order order);
    Collection<Order> findAllOrders();
    Collection<Order> findOrdersByPeriod(Date from, Date to);
}
