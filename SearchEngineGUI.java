import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class SearchEngineGUI extends JFrame {

    private JTextField searchField;
    private JList<String> resultList;
    private DefaultListModel<String> resultListModel;
    private JButton selectDirectoryButton;
    private JButton searchButton;
    private JLabel statusLabel;
    private JLabel selectedDirectoryLabel;
    private JButton aboutButton;
    private JButton historyButton;
    private JButton clearHistoryButton;
    private JFileChooser directoryChooser;
    private FileSearcher fileSearcher;
    private ArrayList<String> searchHistory;
    private File selectedDirectory;
    private JTextPane previewTextPane; // Preview panel for displaying file content

    public SearchEngineGUI() {
        setTitle("MySearchEngine");

        searchHistory = new ArrayList<>();

        JLabel searchLabel = new JLabel("Enter search term:");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        resultListModel = new DefaultListModel<>();
        resultList = new JList<>(resultListModel);
        selectDirectoryButton = new JButton("Select Directory");
        statusLabel = new JLabel();
        directoryChooser = new JFileChooser();
        fileSearcher = new FileSearcher();
        
        searchLabel.setForeground(Color.WHITE);
        
        selectedDirectoryLabel = new JLabel("Selected Directory: ");
        selectedDirectoryLabel.setForeground(Color.BLUE);

        aboutButton = new JButton("About");
        historyButton = new JButton("Search History");
        clearHistoryButton = new JButton("Clear History");
       

        searchButton.addActionListener(e -> performSearch());

        selectDirectoryButton.addActionListener(e -> selectDirectory());

        resultList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = resultList.getSelectedIndex();
                if (selectedIndex != -1) {
                    statusLabel.setText("File Preview Loading...");
                    statusLabel.setForeground(Color.YELLOW);

                    String selectedItem = resultListModel.getElementAt(selectedIndex);

                    // Simulate loading delay with a Timer
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            displayFileContent(selectedItem);
                            statusLabel.setText("Preview Ready");
                            statusLabel.setForeground(Color.GREEN);
                        }
                    });
                    timer.setRepeats(false); // Execute ActionListener only once
                    timer.start();
                }
            }
        });


        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(SearchEngineGUI.this, "MySearchEngine for OOP Assignment - Java " + "\n\n" + "Created by Joshua Sunil Mathew, C22419706", "About", JOptionPane.INFORMATION_MESSAGE));

        historyButton.addActionListener(e -> showSearchHistory());

        clearHistoryButton.addActionListener(e -> clearSearchHistory());

    

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(selectDirectoryButton);
        searchPanel.setBackground(new Color(36, 37, 42)); // Dark gray background
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(selectedDirectoryLabel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(resultList);
        scrollPane.setBackground(new Color(46, 47, 52)); // Darker gray background

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(aboutButton);
        bottomPanel.add(historyButton);
        bottomPanel.add(clearHistoryButton);
        bottomPanel.setBackground(new Color(36, 37, 42)); // Dark gray background

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        mainPanel.setBackground(new Color(46, 47, 52)); // Darker gray background

        // Initialize previewTextPane and its associated style
        previewTextPane = new JTextPane();
        previewTextPane.setEditable(false); // Ensure text pane is not editable
        previewTextPane.setBackground(Color.WHITE); // Set background color
        JScrollPane previewScrollPane = new JScrollPane(previewTextPane);
        previewScrollPane.setPreferredSize(new Dimension(500, 300)); // Set preferred size for preview panel

        // Add preview panel to mainPanel
        mainPanel.add(previewScrollPane, BorderLayout.EAST);

        getContentPane().setBackground(new Color(46, 47, 52)); // Darker gray background
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setVisible(true);
    }

    private void performSearch() {
        if (selectedDirectory == null) {
            JOptionPane.showMessageDialog(this, "Please select a directory.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        resultListModel.clear();
        fileSearcher.performSearch(selectedDirectory, searchTerm, resultListModel, statusLabel);
        if (!searchHistory.contains(searchTerm)) {
            searchHistory.add(searchTerm);
        }
    }

    private void selectDirectory() {
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = directoryChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedDirectory = directoryChooser.getSelectedFile();
            selectedDirectoryLabel.setText("Selected Directory: " + selectedDirectory.getAbsolutePath());
        }
    }

    private void displayFileContent(String selectedItem) {
    	
    
        
        String fileName = selectedItem.split(" - Match Count: ")[0];
        File fileToOpen = new File(selectedDirectory, fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
            StringBuilder fileContent = new StringBuilder();
      
            
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n"); // Append each line to the StringBuilder
            }
            previewTextPane.setText(fileContent.toString()); // Set the content of the JTextPane
            previewTextPane.setCaretPosition(0); // Set caret position to the start
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showSearchHistory() {
        try {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel label = new JLabel("This session's searches: ");
            panel.add(label);

            StringBuilder historyMessage = new StringBuilder();
            for (String term : searchHistory) {
                historyMessage.append(term).append("\n");
            }
            JTextArea historyTextArea = new JTextArea(historyMessage.toString());
            historyTextArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(historyTextArea);
            panel.add(scrollPane);

            JOptionPane.showMessageDialog(this, panel, "Search History", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error displaying search history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearSearchHistory() {
        searchHistory.clear();
        statusLabel.setText("History Cleared!");
        statusLabel.setForeground(Color.RED);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SearchEngineGUI searchEngineGUI = new SearchEngineGUI();
            centerWindow(searchEngineGUI);
        });
    }

    private static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }
}
