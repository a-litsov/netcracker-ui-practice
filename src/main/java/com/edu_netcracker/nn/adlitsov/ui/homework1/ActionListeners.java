package com.edu_netcracker.nn.adlitsov.ui.homework1;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

public class ActionListeners {

    public static class OpenListener implements ActionListener {
        BookModel bookModel;

        public OpenListener(BookModel bookModel) {
            this.bookModel = bookModel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JSON files", "json");
            fc.setFileFilter(filter);
            fc.setAcceptAllFileFilterUsed(false);

            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                if (!bookModel.loadBooks(file)) {
                    JOptionPane.showMessageDialog(null, "Error occurred while books loading!");
                }
            }
        }
    }

    public static class SaveListener implements ActionListener {
        BookModel bookModel;

        public SaveListener(BookModel bookModel) {
            this.bookModel = bookModel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JSON files", "json");
            fc.setFileFilter(filter);
            fc.setAcceptAllFileFilterUsed(false);

            int returnVal = fc.showSaveDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String fileName = fc.getSelectedFile().getAbsolutePath();
                String fileNameWithOutExt = fileName.replaceFirst("[.][^.]+$", "");
                File file = new File(fileNameWithOutExt + "." + filter.getExtensions()[0]);

                bookModel.saveBooks(file);
            }
        }
    }

    public static class SearchBoxListener implements ItemListener {
        JComboBox<Book> bookSearchBox;
        JTextField bookNameField;
        JSpinner booksCountField, bookPriceField;
        List<List<JComponent>> authorsFields;
        JPanel authorsPanel;
        DatePicker bookDatePicker;
        MainFrame frame;

        public SearchBoxListener(JComboBox<Book> bookSearchBox, JTextField bookNameField, JSpinner booksCountField,
                                 JSpinner bookPriceField, List<List<JComponent>> authorsFields, JPanel authorsPanel,
                                 DatePicker bookDatePicker, MainFrame frame) {
            this.bookSearchBox = bookSearchBox;
            this.bookNameField = bookNameField;
            this.booksCountField = booksCountField;
            this.bookPriceField = bookPriceField;
            this.authorsFields = authorsFields;
            this.authorsPanel = authorsPanel;
            this.bookDatePicker = bookDatePicker;
            this.frame = frame;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }

            int selectedIndex = bookSearchBox.getSelectedIndex();
            if (selectedIndex == -1) {
                return;
            }
            Book selectedBook = bookSearchBox.getItemAt(selectedIndex);
            bookNameField.setText(selectedBook.getName());
            booksCountField.setValue(selectedBook.getQty());
            bookPriceField.setValue(selectedBook.getPrice());
            bookDatePicker.setDate(selectedBook.getDate());

            Author[] authors = selectedBook.getAuthors();
            for (int i = 0; i < authors.length; i++) {
                if (i >= authorsFields.size()) {
                    authorsPanel.add(frame.createAuthorPanel(authorsFields));
                }
                List<JComponent> curAuthorField = authorsFields.get(i);
                ((JTextField) curAuthorField.get(0)).setText(authors[i].getName());
                ((JComboBox<Author.Gender>) curAuthorField.get(1)).setSelectedItem(authors[i].getGender());
                ((JTextField) curAuthorField.get(2)).setText(authors[i].getEmail());
            }

            int unusedAuthorFields = authorsFields.size() - authors.length;
            for (int i = 0; i < unusedAuthorFields; i++) {
                frame.removeLastAuthorPanelAndFields(authorsPanel, authorsFields);
            }
            frame.validate();
        }
    }

}
