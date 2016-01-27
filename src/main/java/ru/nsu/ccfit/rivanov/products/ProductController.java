package ru.nsu.ccfit.rivanov.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(value = "products", method = RequestMethod.GET)
    public Collection<Product> getProducts() {
        return productRepository.findAllProducts();
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "products", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody Product product) {
        productRepository.addProduct(product);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "products/{id}", method = RequestMethod.PUT)
    public void updateProduct(@RequestBody Product product, @PathVariable("id") long id) {
        if (!productRepository.tryUpdateProduct(id, product)) {
            throw new ResourceNotFoundException();
        }
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @RequestMapping(value = "products/{id}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable("id") long id) {
        if (!productRepository.tryDeleteProduct(id)) {
            throw new ResourceNotFoundException();
        }
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleNotFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseBody
    public ResponseEntity<String> handleResourceAlreadyExists() {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

}
