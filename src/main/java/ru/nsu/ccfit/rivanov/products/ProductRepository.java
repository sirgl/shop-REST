package ru.nsu.ccfit.rivanov.products;

import com.sun.istack.internal.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface ProductRepository {
    Collection<Product> findAllProducts();

    void addProduct(@NotNull Product product);

    Optional<Product> findProductById(long id);

    boolean tryDecreaseProductAmount(long id, long decreaseCount);

    boolean tryDeleteProduct(long id);

    boolean tryUpdateProduct(long id, @NotNull Product newProduct);
}