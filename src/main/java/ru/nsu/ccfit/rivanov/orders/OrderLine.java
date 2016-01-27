package ru.nsu.ccfit.rivanov.orders;

public class OrderLine {
    private long productId;
    private long amount;

    public OrderLine() {
    }

    public OrderLine(long productId, long amount) {
        this.productId = productId;
        this.amount = amount;
    }

    public long getProductId() {
        return productId;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderLine orderLine = (OrderLine) o;

        return productId == orderLine.productId && amount == orderLine.amount;

    }

    @Override
    public int hashCode() {
        int result = (int) (productId ^ (productId >>> 32));
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "productId=" + productId +
                ", amount=" + amount +
                '}';
    }
}
