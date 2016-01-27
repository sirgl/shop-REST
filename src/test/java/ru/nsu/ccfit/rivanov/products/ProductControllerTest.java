package ru.nsu.ccfit.rivanov.products;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class ProductControllerTest {
    private final ProductController controller = new ProductController();

    @Test
    public void testGetProducts() throws Exception {
        ProductRepository repository = mock(ProductRepository.class);
        List<Product> products = new ArrayList<>();
        products.add(new Product(1));
        when(repository.findAllProducts())
                .thenReturn(products);
        controller.setProductRepository(repository);

        assertThat(controller.getProducts())
                .isEqualTo(products);
    }


    @Test
    public void testAddProduct() throws Exception {
        ProductRepository repository = mock(ProductRepository.class);
        Product product = new Product(1);
        controller.setProductRepository(repository);

        controller.addProduct(product);
        verify(repository, times(1)).addProduct(product);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductRepository repository = mock(ProductRepository.class);
        Product product = new Product(1);
        controller.setProductRepository(repository);
        when(repository.tryUpdateProduct(1, product))
                .thenReturn(true);

        controller.updateProduct(product, 1);
        verify(repository, times(1)).tryUpdateProduct(1, product);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateProductNotFound() throws Exception {
        ProductRepository repository = mock(ProductRepository.class);
        Product product = new Product(1);
        controller.setProductRepository(repository);
        when(repository.tryUpdateProduct(1, product))
                .thenReturn(false);

        controller.updateProduct(product, 1);
        verify(repository, times(1)).tryUpdateProduct(1, product);
    }

    @Test
    public void testDeleteProduct() throws Exception {
        ProductRepository repository = mock(ProductRepository.class);
        controller.setProductRepository(repository);
        when(repository.tryDeleteProduct(1))
                .thenReturn(true);

        controller.deleteProduct(1);
        verify(repository, times(1)).tryDeleteProduct(1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteProductNotFound() throws Exception {
        ProductRepository repository = mock(ProductRepository.class);
        controller.setProductRepository(repository);
        when(repository.tryDeleteProduct(1))
                .thenReturn(false);

        controller.deleteProduct(1);
        verify(repository, times(1)).tryDeleteProduct(1);
    }

    @Test
    public void testHandleNotFound() throws Exception {
        assertThat(controller.handleNotFound())
                .isEqualTo(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Test
    public void testHandleResourceAlreadyExists() throws Exception {
        assertThat(controller.handleResourceAlreadyExists())
                .isEqualTo(new ResponseEntity<>(HttpStatus.CONFLICT));
    }
}