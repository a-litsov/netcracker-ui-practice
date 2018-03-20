package com.edu_netcracker.nn.adlitsov.ui.homework1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
    public static final int WIDTH = 550, HEIGHT = 500;
    private JSplitPane splitPane;
    private JTabbedPane tabbedPane;
    private JScrollPane tablePane;
    private BookModel bookModel;
    private JTable bookTable;

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
        createTabbedPaneForBookOperations();
        createBookTable();

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, tablePane);

        add(splitPane);

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


    private void createBookTable() {
        bookModel = new BookModel();
        bookTable = new JTable(bookModel);
        tablePane = new JScrollPane(bookTable);
        tablePane.setPreferredSize(new Dimension(100, 100));

    }

    private void createTabbedPaneForBookOperations() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add book", createBookAddingTab());

    }

    private JPanel createBookAddingTab() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridLayout(2, 1));

        JPanel mainInfoPanel = new JPanel();
        mainInfoPanel.setBorder(BorderFactory.createTitledBorder("Main book info"));

        mainInfoPanel.add(createPanel(new JLabel("Name:"), new JTextField(15)));
        mainInfoPanel.add(createPanel(new JLabel("Count:"),
                                      new JSpinner(new SpinnerNumberModel(0, 0, 10_000, 1))));
        mainInfoPanel.add(createPanel(new JLabel("Price:"),
                                      new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1_000.0, 0.01)),
                                      new JLabel("$")));

        subPanel.add(mainInfoPanel, BorderLayout.CENTER);


        JPanel authorsPanel = new JPanel();
        authorsPanel.setBorder(BorderFactory.createTitledBorder("Book authors info"));
        authorsPanel.add(new JButton("+"));
        authorsPanel.add(createPanel(new JLabel("Name:"), new JTextField(20)));
        authorsPanel.add(createPanel(new JLabel("Gender:"), new JTextField(1)));
        authorsPanel.add(createPanel(new JLabel("E-mail:"), new JTextField(15)));

        subPanel.add(authorsPanel, BorderLayout.CENTER);

        mainPanel.add(subPanel, BorderLayout.CENTER);


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(new JButton("Add book"));
        buttonsPanel.add(new JButton("Clear fields"));
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createPanel(Component... comps) {
        JPanel panel = new JPanel();
        for (Component comp : comps) {
            panel.add(comp);
        }

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
