package ru.nsu.ccfit.rivanov.products;

import com.sun.istack.internal.NotNull;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Not for production, just to demonstrate working service
 */
@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final List<Product> products = new ArrayList<>();
    private final Object lock = new Object();
    private long idCounter = 0;


    @Override
    public Collection<Product> findAllProducts() {
        synchronized (lock) {
            return products.stream()
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void addProduct(@NotNull Product product) {
        synchronized (lock) {
            product.setId(idCounter++);
            products.add(new Product(product));
        }
    }

    @Override
    public Optional<Product> findProductById(long id) {
        synchronized (lock) {
            return products.stream()
                    .filter(product -> product.getId() == id)
                    .findFirst();
        }
    }

    @Override
    public boolean tryDecreaseProductAmount(long id, long decreaseCount) {
        synchronized (lock) {
            Optional<Product> productOptional = findProductById(id);
            if (!productOptional.isPresent()) {
                return false;
            }
            Product product = productOptional.get();
            if (!(product.getAmount() - decreaseCount >= 0)) {
                return false;
            }
            product.setAmount(product.getAmount() - decreaseCount);
            return true;
        }
    }

    @Override
    public boolean tryDeleteProduct(long id) {
        synchronized (lock) {
            Optional<Product> product = findProductById(id);
            if (product.isPresent()) {
                products.remove(product.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean tryUpdateProduct(long id, @NotNull Product newProduct) {
        synchronized (lock) {
            Optional<Product> productOptional = findProductById(id);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                long oldId = product.getId();
                products.remove(product);
                products.add(newProduct);
                newProduct.setId(oldId);
                return true;
            }
        }
        return false;
    }
}
