package com.edu_netcracker.nn.adlitsov.ui.homework1;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BookModel extends AbstractTableModel {

    private List<Book> books = new ArrayList<>();

    public void addBook(Book b) {
        books.add(b);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book cur = books.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return cur.getName();
            case 1:
                return cur.getAuthor().toString();
            case 2:
                return cur.getPrice();
            case 3:
                return cur.getQty();
        }
        return null;
    }

    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Book name";
            case 1:
                return "Author";
            case 2:
                return "Price";
            case 3:
                return "Count";
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
        }
        return Object.class;
    }
}
