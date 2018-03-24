package com.edu_netcracker.nn.adlitsov.ui.homework1;

import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class MainFrame extends JFrame {
    public static final int WIDTH = 800, HEIGHT = 520;
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
        JMenuItem openMenuItem, saveMenuItem;
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        openMenuItem = new JMenuItem("Open");
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openMenuItem.addActionListener(new ActionListeners.OpenListener(bookModel));
        fileMenu.add(openMenuItem);

        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMenuItem.addActionListener(new ActionListeners.SaveListener(bookModel));
        fileMenu.add(saveMenuItem);
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
        bookNameField.setInputVerifier(new InputVerifiers.BookNameVerifier());
        JSpinner booksCountField = new JSpinner(new SpinnerNumberModel(1, 1, 10_000, 1));
        JSpinner bookPriceField = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1_000.0, 0.01));
        DatePicker bookDatePicker = createAndSetUpDatePicker();
        JPanel mainInfoPanel = createBookMainInfoPanel(bookNameField, booksCountField, bookPriceField, bookDatePicker);
        mainPanel.add(mainInfoPanel, BorderLayout.NORTH);

        // Authors info panel
        List<List<JComponent>> authorsFields = new ArrayList<>();
        JPanel authorsPanel = createAndPutAuthorsScrollPane(authorsFields, mainPanel);

        // Buttons panel and actions for buttons
        JPanel buttonsPanel = createButtonsPanelForAddingTab(bookNameField, booksCountField, bookPriceField, authorsPanel,
                                                             authorsFields, bookDatePicker);
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
        bookNameField.setInputVerifier(new InputVerifiers.BookNameVerifier());
        JSpinner booksCountField = new JSpinner(new SpinnerNumberModel(1, 1, 10_000, 1));
        JSpinner bookPriceField = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1_000.0, 0.01));
        DatePicker bookDatePicker = createAndSetUpDatePicker();
        bookDatePicker.getSettings().setVetoPolicy(date -> date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()));
        JPanel mainInfoPanel = createBookMainInfoPanel(bookNameField, booksCountField, bookPriceField, bookDatePicker);
        bookMainPanel.add(mainInfoPanel);
        tabMainPanel.add(bookMainPanel, BorderLayout.NORTH);

        // Authors info panel
        List<List<JComponent>> authorsFields = new ArrayList<>();
        JPanel authorsPanel = createAndPutAuthorsScrollPane(authorsFields, tabMainPanel);

        // Buttons panel and actions for buttons: TO DO separate method for editing tab
        JPanel buttonsPanel = createButtonsPanelForEditingTab(bookSearchBox, bookNameField, booksCountField,
                                                              bookPriceField, authorsPanel, authorsFields, bookDatePicker);
        tabMainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Action when book is selected
        bookSearchBox.addItemListener(new ActionListeners.SearchBoxListener(bookSearchBox, bookNameField, booksCountField,
                                                                            bookPriceField, authorsFields, authorsPanel,
                                                                            bookDatePicker, this));

        return tabMainPanel;
    }

    private JPanel createBookMainInfoPanel(JTextField bookNameField, JSpinner booksCountField, JSpinner bookPriceField,
                                           DatePicker bookDatePicker) {
        JPanel mainInfoPanel = new JPanel();
        mainInfoPanel.setBorder(BorderFactory.createTitledBorder("Main book info"));

        mainInfoPanel.add(createPanel(new JLabel("Name:"), bookNameField));
        mainInfoPanel.add(createPanel(new JLabel("Count:"), booksCountField));
        mainInfoPanel.add(createPanel(new JLabel("Price:"), bookPriceField, new JLabel("$")));
        mainInfoPanel.add(createPanel(new JLabel("Date:"), bookDatePicker));

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
    public JPanel createAuthorPanel(List<List<JComponent>> authorsFields) {
        JPanel authorPanel = new JPanel();
        authorPanel.setBorder(BorderFactory.createTitledBorder("Author info"));

        JTextField name = new JTextField(20);
        name.setInputVerifier(new InputVerifiers.AuthorNameVerifier());

        JTextField email = new JTextField(15);
        email.setInputVerifier(new InputVerifiers.AuthorEmailVerifier());

        JComboBox<Author.Gender> gender = new JComboBox<>(Author.Gender.values());

        authorPanel.add(createPanel(new JLabel("Name:"), name));
        authorPanel.add(createPanel(new JLabel("Gender:"), gender));
        authorPanel.add(createPanel(new JLabel("E-mail:"), email));


        List<JComponent> fields = new ArrayList<>();
        fields.add(name);
        fields.add(gender);
        fields.add(email);
        authorsFields.add(fields);

        return authorPanel;
    }

    private JPanel createButtonsPanelForAddingTab(JTextField bookNameField, JSpinner booksCountField, JSpinner bookPriceField,
                                                  JPanel authorsPanel, List<List<JComponent>> authorsFields, DatePicker bookDatePicker) {
        JPanel buttonsPanel = new JPanel();

        JButton addBookButton = new JButton("Add book");
        addBookButton.addActionListener(event -> {
            try {
                Author[] authors = parseAuthorsInfo(authorsFields);

                Book book = new Book(bookNameField.getText(), authors, (double) bookPriceField.getValue(),
                                     (int) booksCountField.getValue(), bookDatePicker.getDate());
                bookModel.addBooks(book);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Incorrect book data!");
            }

        });
        buttonsPanel.add(addBookButton);

        buttonsPanel.add(createButton("Add author", event -> {
            authorsPanel.add(createAuthorPanel(authorsFields));
            validate();
        }));

        buttonsPanel.add(createButton("Remove last author", event -> {
            removeLastAuthorPanelAndFields(authorsPanel, authorsFields);
            validate();
        }));

        buttonsPanel.add(createButton("Reset fields",
                                      event -> clearAllFields(bookNameField, booksCountField, bookPriceField,
                                                              authorsFields, bookDatePicker)));

        return buttonsPanel;
    }

    private JPanel createButtonsPanelForEditingTab(JComboBox<Book> bookSearchBox, JTextField bookNameField, JSpinner booksCountField,
                                                   JSpinner bookPriceField, JPanel authorsPanel, List<List<JComponent>> authorsFields,
                                                   DatePicker bookDatePicker) {
        JPanel buttonsPanel = new JPanel();

        JButton applyButton = new JButton("Apply changes");
        applyButton.addActionListener(event -> {
            try {
                int selectedIndex = bookSearchBox.getSelectedIndex();
                if (selectedIndex == -1) {
                    return;
                }


                Book selectedBooks = bookSearchBox.getItemAt(selectedIndex);
                Author[] authors = parseAuthorsInfo(authorsFields);

                bookModel.modifyBooks(selectedBooks, bookNameField.getText(), authors, (double) bookPriceField.getValue(),
                                      (int) booksCountField.getValue(), bookDatePicker.getDate());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Incorrect book data!");
            }
        });
        buttonsPanel.add(applyButton);

        buttonsPanel.add(createButton("Add author", (event) -> {
            authorsPanel.add(createAuthorPanel(authorsFields));
            validate();
        }));

        buttonsPanel.add(createButton("Remove last author", (event) -> {
            removeLastAuthorPanelAndFields(authorsPanel, authorsFields);
            validate();
        }));

        buttonsPanel.add(createButton("Remove books", (event) -> {
            try {
                Author[] authors = parseAuthorsInfo(authorsFields);

                Book book = new Book(bookNameField.getText(), authors, (double) bookPriceField.getValue(),
                                     (int) booksCountField.getValue(), bookDatePicker.getDate());

                bookModel.removeBooks(book);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Incorrect book data!");
            }
        }));

        buttonsPanel.add(createButton("Clear fields", (event) -> {
            clearAllFields(bookNameField, booksCountField, bookPriceField, authorsFields, bookDatePicker);
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
                                List<List<JComponent>> authorsFields, DatePicker bookDatePicker) {
        final String defaultText = "";
        final int defaultCount = 1;
        final double defaultPrice = 0;

        bookNameField.setText(defaultText);
        booksCountField.setValue(defaultCount);
        bookPriceField.setValue(defaultPrice);
        bookDatePicker.setDate(LocalDate.now());

        for (List<JComponent> compsList : authorsFields) {
            ((JTextField) compsList.get(0)).setText(defaultText);
            ((JTextField) compsList.get(2)).setText(defaultText);
            ((JComboBox) compsList.get(1)).setSelectedItem(Author.Gender.MALE);
        }
    }

    public void removeLastAuthorPanelAndFields(JPanel authorsPanel, List<List<JComponent>> authorsFields) {
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

    private JPanel createBookSearchPanelWithAutocomplete(JComboBox bookSearchBox) {
        JPanel bookSearchPanel = new JPanel();
        bookSearchPanel.setBorder(BorderFactory.createTitledBorder("Book search"));

        bookSearchBox.setPrototypeDisplayValue(BOOK_INFO_PROTOTYPE);
        AutoCompleteSupport.install(bookSearchBox, bookModel.getBooks());
        bookSearchPanel.add(createPanel(new JLabel("Start typing book title:"), bookSearchBox));

        bookSearchPanel.add(bookSearchBox);
        return bookSearchPanel;
    }

    private JButton createButton(String title, ActionListener al) {
        JButton btn = new JButton(title);
        btn.addActionListener(al);

        return btn;
    }

    private JScrollPane createBookTable() {
        JTable bookTable = new JTable(bookModel);

        return new JScrollPane(bookTable);
    }

    private DatePicker createAndSetUpDatePicker() {
        DatePicker bookDatePicker = new DatePicker();
        DatePickerSettings settings = bookDatePicker.getSettings();
        settings.setVetoPolicy(date -> date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()));
        settings.setAllowEmptyDates(false);
        bookDatePicker.setDate(LocalDate.now());

        return bookDatePicker;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
