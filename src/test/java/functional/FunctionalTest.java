package functional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.nsu.ccfit.rivanov.config.WebMvcApplicationInitializer;
import ru.nsu.ccfit.rivanov.products.Product;
import ru.nsu.ccfit.rivanov.security.HeaderProcessingFilter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * I know, that I can use Spring tests, but I wanted to try Unirest as a client, despite that
 * fact, that it is much slower
 */
public class FunctionalTest {

    private static final String API_URL = "http://localhost:8080" + WebMvcApplicationInitializer.API_PATH;
    private Product product1;
    private Product product2;
    private Product product3;

    private String authToken;

    @BeforeClass
    public static void initTests() {
        Unirest.setObjectMapper(new ObjectMapper() {
            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();

            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return mapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return mapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Before
    public void setUp() throws Exception {
        product1 = new Product("1", "1", new BigDecimal(100), 10);
        product2 = new Product("2", "2", new BigDecimal(200), 20);
        product3 = new Product("3", "3", new BigDecimal(300), 30);

        authToken = requestAuth();

        requestAddProduct(product1);
        requestAddProduct(product2);
        requestAddProduct(product3);
    }

    @After
    public void tearDown() throws Exception {
        List<Product> products = requestProducts();
        for (Product product : products) {
            requestDelete(product.getId());
        }
    }

    @Test
    public void testGetProductsAfterAddition() throws Exception {
        List<Product> products = requestProducts();

        assertThat(products)
                .containsOnly(product1, product2, product3);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        List<Product> initialProducts = requestProducts();
        long id = initialProducts.get(1).getId();
        Product newProduct = new Product("4", "4", new BigDecimal(400), 40);
        requestUpdate(id, newProduct);
        List<Product> products = requestProducts();

        assertThat(products)
                .containsOnly(product1, newProduct, product3);
    }

    @Test
    public void testDeleteProduct() throws Exception {
        List<Product> initialProducts = requestProducts();
        long id = initialProducts.get(1).getId();
        requestDelete(id);
        List<Product> products = requestProducts();

        assertThat(products)
                .containsOnly(product1, product3);
    }

    private String requestAuth() throws UnirestException {
        HttpResponse<String> response = Unirest.post(API_URL + "login")
                .header("Content-Type", "application/json")
                .header("login", "admin")
                .header("password", "password")
                .asString();
        return response.getBody().substring(1, 41);
    }

    private int requestAddProduct(Product product) throws UnirestException {
        HttpResponse<String> response = Unirest.post(API_URL + "products")
                .header("Content-Type", "application/json")
                .header(HeaderProcessingFilter.HEADER_NAME, authToken)
                .body(product)
                .asString();
        return response.getStatus();
    }

    private List<Product> requestProducts() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(API_URL + "products")
                .asJson();
        return Util.fromJSON(new TypeReference<List<Product>>() {
                             },
                response.getBody().toString());
    }

    private int requestDelete(long id) throws UnirestException {
        HttpResponse<String> response = Unirest.delete(API_URL + "products/" + id)
                .header(HeaderProcessingFilter.HEADER_NAME, authToken)
                .asString();
        return response.getStatus();
    }

    private int requestUpdate(long id, Product newProduct) throws UnirestException {
        HttpResponse<String> response = Unirest.put(API_URL + "products/" + id)
                .header("Content-Type", "application/json")
                .header(HeaderProcessingFilter.HEADER_NAME, authToken)
                .body(newProduct)
                .asString();
        return response.getStatus();
    }
}
