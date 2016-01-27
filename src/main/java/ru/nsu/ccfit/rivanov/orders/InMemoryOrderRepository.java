package ru.nsu.ccfit.rivanov.orders;

import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.rivanov.products.ProductRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryOrderRepository implements OrderRepository {
    private List<Order> orders = new ArrayList<>();
    private final Object lock = new Object();

    @Autowired
    private ProductRepository productRepository;

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addOrder(@NotNull Order order) {
        synchronized (lock) {
            if (!findAllOrders().contains(order)) {
                boolean isValid = order.getOrderLines().stream()
                        .allMatch(orderLine -> productRepository.findProductById(orderLine.getProductId()).isPresent());
                if(!isValid) {
                    throw new BadOrderException();
                }
                orders.add(order);
            }
        }
    }

    @Override
    public Collection<Order> findAllOrders() {
        synchronized (lock) {
            return orders.stream()
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<Order> findOrdersByPeriod(Date from, Date to) {
        synchronized (lock) {
            return orders.stream()
                    .filter(order -> order.getDate().after(from) && order.getDate().before(to))
                    .collect(Collectors.toList());
        }
    }
}
