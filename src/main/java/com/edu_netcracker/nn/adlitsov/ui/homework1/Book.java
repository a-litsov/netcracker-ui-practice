package com.edu_netcracker.nn.adlitsov.ui.homework1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class Book {
    private String name;
    private Author[] authors;
    private double price;
    private int qty = 1;
    private LocalDate date;

    public Book(String name, Author[] authors, double price) {
        validateName(name);
        validateAuthors(authors);
        validatePrice(price);

        this.name = name;

        this.authors = Arrays.copyOf(authors, authors.length);
        Arrays.sort(this.authors);

        this.price = price;
    }

    public Book(String name, Author[] authors, double price, int qty) {
        this(name, authors, price);

        validateQty(qty);
        this.qty = qty;
    }

    @JsonCreator
    public Book(@JsonProperty("name") String name, @JsonProperty("authors") Author[] authors, @JsonProperty("price") double price,
                @JsonProperty("qty") int qty, @JsonProperty("date") LocalDate date) {
        this(name, authors, price, qty);

        this.date = date;
    }

    private void validateName(String name) {
        if (name == null || !isNameValid(name)) {
            throw new IllegalArgumentException("Incorrect name!");
        }
    }

    public static boolean isNameValid(String name) {
        return name.toLowerCase().matches("^[a-zа-я0-9().,?!;:/'\"-+= ]+$");
    }

    private void validateAuthors(Author[] authors) {
        if (authors == null || authors.length == 0) {
            throw new IllegalArgumentException("At least 1 author must be specified");
        }

        for (Author author : authors) {
            if (author == null) {
                throw new IllegalArgumentException("All author references in authors array must be not null");
            }
        }
    }

    private void validatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price must be non-negative number");
        }
    }

    private void validateQty(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Qty must be non-negative integral number");
        }
    }

    private void validateDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Book must be written already!");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author[] getAuthors() {
        return Arrays.copyOf(authors, authors.length);
    }

    public void setAuthors(Author[] authors) {
        validateAuthors(authors);

        this.authors = Arrays.copyOf(authors, authors.length);
        Arrays.sort(this.authors);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        validatePrice(price);
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        validateQty(qty);
        this.qty = qty;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        validateDate(date);
        this.date = date;
    }

    @Override
    public boolean equals(Object anotherObj) {
        if (anotherObj == this) {
            return true;
        }

        if (anotherObj == null) {
            return false;
        }

        if (getClass() != anotherObj.getClass()) {
            return false;
        }

        Book other = (Book) anotherObj;
        return Objects.equals(name, other.name) && Arrays.equals(authors, other.authors) && price == other.price
                && Objects.equals(date, other.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, date) + Arrays.hashCode(authors);
    }

    @Override
    public String toString() {
        return name + ", " + getAuthorNames() + ((date != null) ? ", " + date.getYear() : "");
    }

    @JsonIgnore
    public String getAuthorNames() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < authors.length; i++) {
            sb.append(authors[i].getName()).append((i < authors.length - 1) ? ", " : "");
        }
        return sb.toString();
    }
}
