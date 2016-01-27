package ru.nsu.ccfit.rivanov.products;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class InMemoryProductRepositoryDecreaseTest {
    private final InMemoryProductRepository repository = new InMemoryProductRepository();

    private long amount;
    private long decreaseCount;
    private long expectedAmount;
    private boolean successExpected;

    public InMemoryProductRepositoryDecreaseTest(Params params) {
        this.amount = params.amount;
        this.decreaseCount = params.decreaseCount;
        this.expectedAmount = params.expectedAmount;
        this.successExpected = params.successExpected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
                {new Params(10, 4, 6, true)},
                {new Params(10, 10, 0, true)},
                {new Params(10, 9, 1, true)},
                {new Params(10, 0, 10, true)},
                {new Params(0, 10, 0, false)},
                {new Params(10, 15, 10, false)}
        });
    }

    @Test
    public void testDecreaseAmount() throws Exception {
        Product product = new Product(99);
        product.setAmount(amount);
        repository.addProduct(product);

        repository.tryDecreaseProductAmount(0, decreaseCount);
        assertThat(repository.findProductById(0).get().getAmount())
                .isEqualTo(expectedAmount);
    }

    @Test
    public void testDecreaseAmountSuccessExpected() throws Exception {
        Product product = new Product(99);
        product.setAmount(amount);
        repository.addProduct(product);

        assertThat(repository.tryDecreaseProductAmount(0, decreaseCount))
                .isEqualTo(successExpected);
    }

    public static class Params {
        public long amount;
        public long decreaseCount;
        public long expectedAmount;
        public boolean successExpected;

        public Params(long amount, long decreaseCount, long expectedAmount, boolean successExpected) {
            this.amount = amount;
            this.decreaseCount = decreaseCount;
            this.expectedAmount = expectedAmount;
            this.successExpected = successExpected;
        }
    }
}
