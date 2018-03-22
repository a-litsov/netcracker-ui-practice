package com.edu_netcracker.nn.adlitsov.ui.homework1;

import ca.odell.glazedlists.swing.AutoCompleteSupport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    public static final int WIDTH = 730, HEIGHT = 520;
    public static final String BOOK_INFO_PROTOTYPE = "Core Java, Volume 1: Fundamentals, Cay S. Horstmann";

    private final BookModel bookModel = new BookModel();

    public MainFrame() {
        super("Book Storage");
        setSize(WIDTH, HEIGHT);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        final int screenWidth = screenSize.width;
        final int screenHeight = screenSize.height;
        setLocation((screenWidth - WIDTH) / 2, (screenHeight - HEIGHT) / 2);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        createMenuBar();
        createSplitPane();


        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar;
        JMenu fileMenu;
        JMenuItem openMenutItem, saveMenuItem;

        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        openMenutItem = new JMenuItem("Open");
        openMenutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

        fileMenu.add(openMenutItem);

        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        fileMenu.add(saveMenuItem);

        setJMenuBar(menuBar);
    }

    private void createSplitPane() {
        JTabbedPane tabbedPane = createTabbedPaneForBookOperations();
        JScrollPane tablePane = createBookTable();
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, tablePane);
        splitPane.setOneTouchExpandable(true);

        add(splitPane);
    }

    private JTabbedPane createTabbedPaneForBookOperations() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add book", createBookAddingTab());
        tabbedPane.addTab("Edit/remove book", createBookEditingTab());

        return tabbedPane;
    }

    private JPanel createBookAddingTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Book main info fields (such as name, price, count, etc.) without authors info
        JTextField bookNameField = new JTextField(15);
        JSpinner booksCountField = new JSpinner(new SpinnerNumberModel(0, 0, 10_000, 1));
        JSpinner bookPriceField = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1_000.0, 0.01));
        JPanel mainInfoPanel = createBookMainInfoPanel(bookNameField, booksCountField, bookPriceField);
        mainPanel.add(mainInfoPanel, BorderLayout.NORTH);

        // Authors info panel
        List<List<JComponent>> authorsFields = new ArrayList<>();
        JPanel authorsPanel = createAndPutAuthorsScrollPane(authorsFields, mainPanel);

        // Buttons panel and actions for buttons
        JPanel buttonsPanel = createButtonsPanelForAddingTab(bookNameField, booksCountField, bookPriceField, authorsPanel,
                                                             authorsFields);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createBookEditingTab() {
        JPanel tabMainPanel = new JPanel(new BorderLayout());

        // Book searching and main info fields (such as name, price, count, etc.) without authors info
        JPanel bookMainPanel = new JPanel(new GridLayout(0, 1));

        JComboBox<Book> bookSearchBox = new JComboBox<>();
        JPanel bookSearchPanel = createBookSearchPanelWithAutocomplete(bookSearchBox);
        bookSearchPanel.add(bookSearchBox);
        bookMainPanel.add(bookSearchPanel);

        JTextField bookNameField = new JTextField(15);
        JSpinner booksCountField = new JSpinner(new SpinnerNumberModel(0, 0, 10_000, 1));
        JSpinner bookPriceField = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1_000.0, 0.01));
        JPanel mainInfoPanel = createBookMainInfoPanel(bookNameField, booksCountField, bookPriceField);
        bookMainPanel.add(mainInfoPanel);
        tabMainPanel.add(bookMainPanel, BorderLayout.NORTH);

        // Authors info panel
        List<List<JComponent>> authorsFields = new ArrayList<>();
        JPanel authorsPanel = createAndPutAuthorsScrollPane(authorsFields, tabMainPanel);

        // Buttons panel and actions for buttons: TO DO separate method for editing tab
        JPanel buttonsPanel = createButtonsPanelForEditingTab(bookSearchBox, bookNameField, booksCountField,
                                                              bookPriceField, authorsPanel, authorsFields);
        tabMainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Action when book is selected
        createBookSearchBoxListener(bookSearchBox, bookNameField, booksCountField, bookPriceField, authorsFields, authorsPanel);

        return tabMainPanel;
    }

    private JPanel createBookMainInfoPanel(JTextField bookNameField, JSpinner booksCountField, JSpinner bookPriceField) {
        JPanel mainInfoPanel = new JPanel();
        mainInfoPanel.setBorder(BorderFactory.createTitledBorder("Main book info"));

        mainInfoPanel.add(createPanel(new JLabel("Name:"), bookNameField));
        mainInfoPanel.add(createPanel(new JLabel("Count:"), booksCountField));
        mainInfoPanel.add(createPanel(new JLabel("Price:"), bookPriceField, new JLabel("$")));

        return mainInfoPanel;
    }

    /*
     * Creates scroll pane for multiple authors (inside method only one 'starting' author panel added via
     * createAuthorPanel() method) and locates it in the BorderLayout.CENTER of parentPanel.
     * parentPanel must use BorderLayout; returned panel is component of created scroll pane where all author panels
     * must be placed.
     */
    private JPanel createAndPutAuthorsScrollPane(List<List<JComponent>> authorsFields, JPanel parentPanel) {
        JPanel authorsPanel = new JPanel(new GridLayout(0, 1));

        authorsPanel.add(createAuthorPanel(authorsFields));

        JScrollPane authorsScrollPane = new JScrollPane(authorsPanel);
        Dimension authorsPanelMinSize = authorsPanel.getMinimumSize();
        authorsPanelMinSize.height += 5;
        authorsScrollPane.setMinimumSize(authorsPanelMinSize);
        authorsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        parentPanel.add(authorsScrollPane, BorderLayout.CENTER);

        return authorsPanel;
    }

    /*
     * Receives list and populates it with new author panel data fields
     */
    private JPanel createAuthorPanel(List<List<JComponent>> authorsFields) {
        JPanel authorPanel = new JPanel();
        authorPanel.setBorder(BorderFactory.createTitledBorder("Author info"));

        JTextField name, email;
        JComboBox<Author.Gender> gender;
        authorPanel.add(createPanel(new JLabel("Name:"), name = new JTextField(20)));
        authorPanel.add(createPanel(new JLabel("Gender:"), gender = new JComboBox<>(Author.Gender.values())));
        authorPanel.add(createPanel(new JLabel("E-mail:"), email = new JTextField(15)));

        List<JComponent> fields = new ArrayList<>();
        fields.add(name);
        fields.add(gender);
        fields.add(email);
        authorsFields.add(fields);

        return authorPanel;
    }

    private JPanel createButtonsPanelForAddingTab(JTextField bookNameField, JSpinner booksCountField, JSpinner bookPriceField,
                                                  JPanel authorsPanel, List<List<JComponent>> authorsFields) {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(createButton("Add book", (event) -> {
            Author[] authors = parseAuthorsInfo(authorsFields);

            Book book = new Book(bookNameField.getText(), authors, (double) bookPriceField.getValue(),
                                 (int) booksCountField.getValue());
            bookModel.addBook(book);
        }));
        buttonsPanel.add(createButton("Add author", (event) -> {
            authorsPanel.add(createAuthorPanel(authorsFields));
            validate();
        }));
        buttonsPanel.add(createButton("Remove last author", (event) -> {
            removeLastAuthorPanelAndFields(authorsPanel, authorsFields);
            validate();
        }));
        buttonsPanel.add(createButton("Clear fields",
                                      (event) -> clearAllFields(bookNameField, booksCountField, bookPriceField, authorsFields)));

        return buttonsPanel;
    }

    private JPanel createButtonsPanelForEditingTab(JComboBox<Book> bookSearchBox, JTextField bookNameField, JSpinner booksCountField,
                                                   JSpinner bookPriceField, JPanel authorsPanel, List<List<JComponent>> authorsFields) {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(createButton("Apply changes", (event) -> {
            int selectedIndex = bookSearchBox.getSelectedIndex();
            if (selectedIndex == -1) {
                return;
            }

            Book selectedBook = bookSearchBox.getItemAt(selectedIndex);
            selectedBook.setName(bookNameField.getText());
            selectedBook.setPrice((double) bookPriceField.getValue());
            selectedBook.setQty((int) booksCountField.getValue());
            selectedBook.setAuthors(parseAuthorsInfo(authorsFields));

            bookModel.dataChanged();
        }));

        buttonsPanel.add(createButton("Remove books", (event) -> {
            int selectedIndex = bookSearchBox.getSelectedIndex();
            if (selectedIndex == -1) {
                return;
            }

            Book selectedBook = bookSearchBox.getItemAt(selectedIndex);
            int removeQty = (int) booksCountField.getValue();
            bookModel.removeBooks(selectedIndex, removeQty);
        }));

        buttonsPanel.add(createButton("Clear fields", (event) -> {
            clearAllFields(bookNameField, booksCountField, bookPriceField, authorsFields);
            bookSearchBox.setSelectedIndex(-1);
        }));

        return buttonsPanel;
    }

    private Author[] parseAuthorsInfo(List<List<JComponent>> authorsFields) {
        Author[] authors = new Author[authorsFields.size()];

        for (int i = 0; i < authors.length; i++) {
            List<JComponent> authorFields = authorsFields.get(i);
            String name = ((JTextField) authorFields.get(0)).getText();
            Author.Gender gender = (Author.Gender) ((JComboBox<Author.Gender>) authorFields.get(1)).getSelectedItem();
            String email = ((JTextField) authorFields.get(2)).getText();

            authors[i] = new Author(name, email, gender);
        }

        return authors;
    }

    private void clearAllFields(JTextField bookNameField, JSpinner booksCountField, JSpinner bookPriceField,
                                List<List<JComponent>> authorsFields) {
        final String defaultText = "";
        final int defaultInt = 0;
        final double defaultDouble = 0;

        bookNameField.setText(defaultText);
        booksCountField.setValue(defaultInt);
        bookPriceField.setValue(defaultDouble);

        for (List<JComponent> compsList : authorsFields) {
            ((JTextField) compsList.get(0)).setText(defaultText);
            ((JTextField) compsList.get(2)).setText(defaultText);
            ((JComboBox) compsList.get(1)).setSelectedIndex(defaultInt);
        }
    }

    private void removeLastAuthorPanelAndFields(JPanel authorsPanel, List<List<JComponent>> authorsFields) {
        int authorsCount = authorsPanel.getComponentCount();
        if (authorsCount > 1) {
            authorsPanel.remove(authorsCount - 1);
            authorsFields.remove(authorsCount - 1);
        }
    }

    private JPanel createPanel(Component... comps) {
        JPanel panel = new JPanel();
        for (Component comp : comps) {
            panel.add(comp);
        }

        return panel;
    }

    private JButton createButton(String title, ActionListener al) {
        JButton btn = new JButton(title);
        btn.addActionListener(al);

        return btn;
    }

    private JPanel createBookSearchPanelWithAutocomplete(JComboBox bookSearchBox) {
        JPanel bookSearchPanel = new JPanel();
        bookSearchPanel.setBorder(BorderFactory.createTitledBorder("Book search"));

        bookSearchBox.setPrototypeDisplayValue(BOOK_INFO_PROTOTYPE);
        AutoCompleteSupport.install(bookSearchBox, bookModel.getBooks());
        bookSearchPanel.add(createPanel(new JLabel("Start typing book title:"), bookSearchBox));

        bookSearchPanel.add(bookSearchBox);
        return bookSearchPanel;
    }

    private void createBookSearchBoxListener(JComboBox<Book> bookSearchBox, JTextField bookNameField, JSpinner booksCountField,
                                             JSpinner bookPriceField, List<List<JComponent>> authorsFields, JPanel authorsPanel) {
        bookSearchBox.addItemListener((event) -> {
            if (event.getStateChange() != ItemEvent.SELECTED) {
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

            Author[] authors = selectedBook.getAuthors();
            for (int i = 0; i < authors.length; i++) {
                if (i >= authorsFields.size()) {
                    authorsPanel.add(createAuthorPanel(authorsFields));
                }
                List<JComponent> curAuthorField = authorsFields.get(i);
                ((JTextField) curAuthorField.get(0)).setText(authors[i].getName());
                ((JComboBox<Author.Gender>) curAuthorField.get(1)).setSelectedItem(authors[i].getGender());
                ((JTextField) curAuthorField.get(2)).setText(authors[i].getEmail());
            }

            int unusedAuthorFields = authorsFields.size() - authors.length;
            for (int i = 0; i < unusedAuthorFields; i++) {
                removeLastAuthorPanelAndFields(authorsPanel, authorsFields);
            }
            validate();
        });
    }

    private JScrollPane createBookTable() {
        JTable bookTable = new JTable(bookModel);

        return new JScrollPane(bookTable);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
