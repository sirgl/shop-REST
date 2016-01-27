package ru.nsu.ccfit.rivanov.orders;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class Order {
    private Collection<OrderLine> orderLines;
    private Date date;

    public Order() {
    }

    public Order(Collection<OrderLine> orderLines) {
        this(orderLines, new Date());
    }

    public Order(Collection<OrderLine> orderLines, Date date) {
        this.orderLines = orderLines;
        this.date = date;
    }

    public Collection<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(Collection<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderLines != null ?  !orderLines.equals(order.orderLines) : order.orderLines != null) return false;
        return !(date != null ? !date.equals(order.date) : order.date != null);

    }

    @Override
    public int hashCode() {
        int result = orderLines != null ? orderLines.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderLines=" + orderLines +
                ", date=" + date.getTime() +
                '}';
    }
}
