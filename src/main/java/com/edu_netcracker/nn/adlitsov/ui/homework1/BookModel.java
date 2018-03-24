package com.edu_netcracker.nn.adlitsov.ui.homework1;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.time.LocalDate;

public class BookModel extends AbstractTableModel {
    private EventList<Book> books = new BasicEventList<>();


    public void addBooks(Book newBook) {
        int index = books.indexOf(newBook);
        if (index == -1) {
            books.add(newBook);
        } else {
            Book existingBook = books.get(index);
            existingBook.setQty(existingBook.getQty() + newBook.getQty());
        }

        fireTableDataChanged();
    }

    public void removeBooks(Book bookToRemove) {
        int index = books.indexOf(bookToRemove);
        if (index != -1) {
            Book existingBook = books.get(index);
            int currentQty = existingBook.getQty();
            int removeQty = bookToRemove.getQty();

            if (currentQty < removeQty) {
                throw new IllegalArgumentException("Cannot remove more books than have");
            }

            if (currentQty == removeQty) {
                books.remove(index);
            } else {
                existingBook.setQty(currentQty - removeQty);
            }

            fireTableDataChanged();
        }
    }


    public void modifyBooks(Book oldBook, String name, Author[] authors, double price, int count, LocalDate date) {
        // do nothing, if oldBook is not found
        int oldIndex = books.indexOf(oldBook);
        if (oldIndex == -1) {
            return;
        }

        // search for equal books
        Book newBook = new Book(name, authors, price, count, date);
        int newIndex = books.indexOf(newBook);
        if (newIndex != -1) {
            Book foundBook = books.get(newIndex);

            // newBook is found: if it's not oldBook, then add newBook qty to found book and remove oldBook,
            // else just update qty
            if (newIndex != oldIndex) {
                oldBook.setQty(foundBook.getQty() + newBook.getQty());
                books.remove(newIndex);
            } else {
                oldBook.setQty(newBook.getQty());
            }
        }

        // modify properties of oldBook except qty
        oldBook.setName(name);
        oldBook.setAuthors(authors);
        oldBook.setPrice(price);
        oldBook.setDate(date);

        fireTableDataChanged();
    }

    public EventList<Book> getBooks() {
        return books;
    }

    public boolean loadBooks(File file) {
        try {
            books.clear();

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // This code only updates books list without creating new (it's needed for combobox with autocompletion)
            ObjectReader reader = mapper.readerForUpdating(books);
            reader.forType(new TypeReference<BasicEventList<Book>>() {
            }).readValue(file);

            // All equal books are grouped and their counts are summed
            Book currentBook, otherBook;
            for (int i = 0; i < books.size(); i++) {
                int j = i + 1;
                while (j < books.size()) {
                    currentBook = books.get(i);
                    otherBook = books.get(j);
                    if (currentBook.equals(otherBook)) {
                        currentBook.setQty(currentBook.getQty() + otherBook.getQty());
                        books.remove(j);
                    } else {
                        j++;
                    }
                }
            }
            fireTableDataChanged();

            return true;
        } catch (Exception ex) {
            books.clear();
            ex.printStackTrace();

            return false;
        }
    }

    public void saveBooks(File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            mapper.writeValue(file, books);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book cur = books.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return cur.getName();
            case 1:
                return cur.getAuthorNames();
            case 2:
                return cur.getPrice();
            case 3:
                return cur.getQty();
            case 4:
                return cur.getDate();
        }
        return null;
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Book name";
            case 1:
                return "Authors";
            case 2:
                return "Price";
            case 3:
                return "Count";
            case 4:
                return "Date";
        }
        return "";
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Double.class;
            case 3:
                return Integer.class;
            case 4:
                return LocalDate.class;
        }
        return Object.class;
    }
}
