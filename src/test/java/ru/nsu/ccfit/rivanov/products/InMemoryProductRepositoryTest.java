package ru.nsu.ccfit.rivanov.products;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryProductRepositoryTest {
    private final InMemoryProductRepository repository = new InMemoryProductRepository();

    @Test
    public void testAddProduct() throws Exception {
        Product product = new Product("TEST", "descr", new BigDecimal(123), 40, 3423);
        repository.addProduct(product);

        assertThat(repository.findAllProducts())
                .as("Added product must be contained among all products")
                .contains(product);
    }

    @Test
    public void testAddProductSameId() throws Exception {
        Product product = new Product(10);
        repository.addProduct(product);
        repository.addProduct(product);

        assertThat(repository.findAllProducts())
                .as("Ids must be assigned to products by repository")
                .containsOnly(new Product(0), new Product(1));
    }

    @Test
    public void testFindProductById() throws Exception {
        Product product = new Product(50);
        repository.addProduct(product);

        assertThat(repository.findProductById(0))
                .as("Added product must be found by id")
                .hasValue(product);
    }

    @Test
    public void testFindProductByIdEmptyRepository() throws Exception {
        assertThat(repository.findProductById(0))
                .as("Empty repository must return empty optional on find")
                .isEmpty();
    }

    @Test
    public void testFindProductByIdNotEmptyRepository() throws Exception {
        Product product = new Product(0);
        repository.addProduct(product);

        assertThat(repository.findProductById(10))
                .as("Not empty repository must return empty optional on find if no such id found")
                .isEmpty();
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = new Product(0);
        repository.addProduct(product);
        repository.tryDeleteProduct(0);

        assertThat(repository.findProductById(0))
                .as("Removed value must not be present in repository")
                .isEmpty();
    }

    @Test
    public void testDeleteProductSuccess() throws Exception {
        Product product = new Product(0);
        repository.addProduct(product);

        assertThat(repository.tryDeleteProduct(0))
                .as("Removing by id of existing product must return true")
                .isTrue();
    }

    @Test
    public void testDeleteProductFailure() throws Exception {
        assertThat(repository.tryDeleteProduct(0))
                .as("Removing by id of not existing product must return false")
                .isFalse();
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = new Product("TEST", "descr", new BigDecimal(123), 40, 1);
        repository.addProduct(product);
        Product newProduct = new Product("1", "d", new BigDecimal(12), 10, 1);

        repository.tryUpdateProduct(0, newProduct);
        Product product1 = repository.findProductById(0).get();
        assertThat(product1.getName())
                .as("Product with specified id must be replaced with new value")
                .isEqualTo("1");
    }

    @Test
    public void testUpdateProductSuccess() throws Exception {
        Product product = new Product("TEST", "descr", new BigDecimal(123), 40, 1);
        repository.addProduct(product);
        Product newProduct = new Product("1", "d", new BigDecimal(12), 10, 1);

        assertThat(repository.tryUpdateProduct(0, newProduct))
                .as("If update was successful it must return true")
                .isTrue();
    }

    @Test
    public void testUpdateProductWrongId() throws Exception {
        Product newProduct = new Product("1", "d", new BigDecimal(12), 10, 1);

        repository.tryUpdateProduct(1, newProduct);
        assertThat(repository.findProductById(1))
                .as("New product must not be added if no such id present in repository")
                .isEmpty();
    }

    @Test
    public void testUpdateProductFailure() throws Exception {
        Product newProduct = new Product("1", "d", new BigDecimal(12), 10, 1);

        assertThat(repository.tryUpdateProduct(1, newProduct))
                .as("If update failed it must return false")
                .isFalse();
    }
}