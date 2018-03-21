package com.edu_netcracker.nn.adlitsov.ui.homework1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    public static final int WIDTH = 730, HEIGHT = 520;

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

        return tabbedPane;
    }

    private JPanel createBookAddingTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel mainInfoPanel = new JPanel();
        mainInfoPanel.setBorder(BorderFactory.createTitledBorder("Main book info"));

        JTextField bookName;
        JSpinner booksCount, bookPrice;
        mainInfoPanel.add(createPanel(new JLabel("Name:"), bookName = new JTextField(15)));
        mainInfoPanel.add(createPanel(new JLabel("Count:"),
                                      booksCount = new JSpinner(new SpinnerNumberModel(0, 0, 10_000, 1))));
        mainInfoPanel.add(createPanel(new JLabel("Price:"),
                                      bookPrice = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1_000.0, 0.01)),
                                      new JLabel("$")));

        mainPanel.add(mainInfoPanel, BorderLayout.NORTH);

        JPanel authorsPanel = new JPanel(new GridLayout(0, 1));

        List<List<JComponent>> authorsFields = new ArrayList<>();
        authorsPanel.add(createAuthorPanel(authorsFields));


        JScrollPane authorsScrollPane = new JScrollPane(authorsPanel);
        Dimension authorsPanelMinSize = authorsPanel.getMinimumSize();
        authorsPanelMinSize.height += 5;
        authorsScrollPane.setMinimumSize(authorsPanelMinSize);
        authorsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(authorsScrollPane, BorderLayout.CENTER);


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(createButton("Add book", (event) -> {
            // TO DO
            System.out.println(authorsFields);
        }));
        buttonsPanel.add(createButton("Add author", (event) -> {
            authorsPanel.add(createAuthorPanel(authorsFields));
            validate();
        }));

        buttonsPanel.add(createButton("Remove last author", (event) -> {
            int authorsCount = authorsPanel.getComponentCount();
            if (authorsCount > 1) {
                authorsPanel.remove(authorsCount - 1);
                authorsFields.remove(authorsCount - 1);
            }
            validate();
        }));

        buttonsPanel.add(createButton("Clear fields", (event) -> {
            // TO DO

            validate();
        }));
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
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

    private JScrollPane createBookTable() {
        JTable bookTable = new JTable(bookModel);

        return new JScrollPane(bookTable);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
