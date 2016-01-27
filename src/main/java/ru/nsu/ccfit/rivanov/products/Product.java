package ru.nsu.ccfit.rivanov.products;

import java.math.BigDecimal;

public class Product {
    private String name;
    private String description;
    private BigDecimal cost;
    private long amount;
    private long id;

    public Product(String name, String description, BigDecimal cost, long amount, long id) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.amount = amount;
        this.id = id;
    }

    public Product(String name, String description, BigDecimal cost, long amount) {
        this(name, description, cost, amount, 0);
    }

    public Product() {
    }

    public Product(Product another) {
        this.name = another.name;
        this.description = another.description;
        this.cost = another.cost;
        this.amount = another.amount;
        this.id = another.id;
    }

    public Product(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (amount != product.amount) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (description != null ? !description.equals(product.description) : product.description != null) return false;
        return !(cost != null ? !cost.equals(product.cost) : product.cost != null);

    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", amount=" + amount +
                ", id=" + id +
                '}';
    }
}
