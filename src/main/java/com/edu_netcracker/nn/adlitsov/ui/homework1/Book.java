package com.edu_netcracker.nn.adlitsov.ui.homework1;

public class Book {
    private String name;
    private Author author;
    private double price;
    private int qty = 0;

    public Book(double price, Author author, String name) {
        this.price = price;
        this.author = author;
        this.name = name;
    }

    public Book(String name, int qty, double price, Author author) {
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public Author getAuthor() {
        return author;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author=" + author +
                ", price=" + price +
                ", qty=" + qty +
                '}';
    }
}
