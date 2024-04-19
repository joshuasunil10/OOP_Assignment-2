/* OBJECT ORIENTED PROGRAMMING ASSIGNMENT 
 * 
 * AUTHOR : JOSHUA SUNIL MATHEW C22419706
 * 
 * MySearchEngine - contains all the components and logic for the MySearchEngine GUI
 
 * 
 * 
 * */


// IMPORTS
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// 
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

    // SEARCH ENGINE GUI CLASS
    public SearchEngineGUI() {
        
    	// set the title of the application window
    	setTitle("MySearchEngine");

    	// initialise the search history as a new a arraylist
        searchHistory = new ArrayList<>();

        // creating components
        
        // search label
        JLabel searchLabel = new JLabel("Enter search term:");
        
        // search field
        searchField = new JTextField(20);
        
        // search button
        searchButton = new JButton("Search");
        
        // resutlts list
        resultListModel = new DefaultListModel<>();
        resultList = new JList<>(resultListModel);
        
        // select directory
        selectDirectoryButton = new JButton("Select Directory");
        
     
        // directory chooser
        directoryChooser = new JFileChooser();
        
        // instance of the FileSearcher Class
        fileSearcher = new FileSearcher();
        
        // status label
        statusLabel = new JLabel();
        
        // search Label white text
        searchLabel.setForeground(Color.WHITE);
        
        // selected directory label and styling
        selectedDirectoryLabel = new JLabel("Selected Directory: ");
        selectedDirectoryLabel.setForeground(Color.BLUE);

        // about window
        aboutButton = new JButton("About");
        
        // search history button
        historyButton = new JButton("Search History");
        
        //clear search history
        clearHistoryButton = new JButton("Clear History");
       
        
        
        // Action Listeners 
        
        // search button functionality -> performSearch 
        searchButton.addActionListener(e -> performSearch());

        
        // select directory functionality
        selectDirectoryButton.addActionListener(e -> selectDirectory());

     
        
        
        // resultlist functionality
        resultList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = resultList.getSelectedIndex();
                if (selectedIndex != -1) {
                	//status update
                    statusLabel.setText("File Preview Loading...");
                    statusLabel.setForeground(Color.YELLOW);

                    String selectedItem = resultListModel.getElementAt(selectedIndex);

                    // Loading Delay
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            displayFileContent(selectedItem);
                            // status update
                            statusLabel.setText("Preview Ready");
                            statusLabel.setForeground(Color.GREEN);
                        }
                    });
                    // actionlistener only activates once
                    timer.setRepeats(false); 
                    timer.start();
                }
            }
        });

        // aboutbutton functionality
        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(SearchEngineGUI.this, "MySearchEngine for OOP Assignment - Java " + "\n\n" + "Created by Joshua Sunil Mathew, C22419706", "About", JOptionPane.INFORMATION_MESSAGE));
        
        // history button functionality
        historyButton.addActionListener(e -> showSearchHistory());

        // clear history functionality
        clearHistoryButton.addActionListener(e -> clearSearchHistory());

    
        // initialse new panels with border and flow layouts respectively
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        
        
        
        // adding components and styling
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(selectDirectoryButton);
        searchPanel.setBackground(new Color(36, 37, 42)); 
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(selectedDirectoryLabel, BorderLayout.SOUTH);

        // scrollpane for results 
        JScrollPane scrollPane = new JScrollPane(resultList);
        scrollPane.setBackground(new Color(46, 47, 52));

        
        // bottom panel for utility 
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(aboutButton);
        bottomPanel.add(historyButton);
        bottomPanel.add(clearHistoryButton);
        
        // color grey
        bottomPanel.setBackground(new Color(36, 37, 42)); 

        	// border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // add components
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // grey coloring
        mainPanel.setBackground(new Color(46, 47, 52)); 

        // Initialize previewTextPane and its associated style
        previewTextPane = new JTextPane();
        // set to non editable
        previewTextPane.setEditable(false);
        previewTextPane.setBackground(Color.WHITE); 
        
        // preview pane
        JScrollPane previewScrollPane = new JScrollPane(previewTextPane);
        previewScrollPane.setPreferredSize(new Dimension(500, 300)); 
        // Add preview panel to mainPanel
        mainPanel.add(previewScrollPane, BorderLayout.EAST);

        // add styling and positioning 
        getContentPane().setBackground(new Color(46, 47, 52)); 
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        
        
        // JFrame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        
        // set frame visible
        setVisible(true);
    }

    // PERFORM SEARCH METHOD
    private void performSearch() {
    	
    	// if the directory is not selected
        if (selectedDirectory == null) {
            JOptionPane.showMessageDialog(this, "Please select a directory.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // if the search term is not entered
        String searchTerm = searchField.getText().trim().toLowerCase();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        // call the filesearcher to search
        resultListModel.clear();
        fileSearcher.performSearch(selectedDirectory, searchTerm, resultListModel, statusLabel);
        if (!searchHistory.contains(searchTerm)) {
            searchHistory.add(searchTerm);
        }
    }
    
    
    
    // SELECT DIRECTORY METHOD
    private void selectDirectory() {
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = directoryChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedDirectory = directoryChooser.getSelectedFile();
            // display the directory
            selectedDirectoryLabel.setText("Selected Directory: " + selectedDirectory.getAbsolutePath());
        }
    }
    
    
    // FILE PREVIEW METHOD with highlighting in bold
    private void displayFileContent(String selectedItem) {
        String fileName = selectedItem.split(" - Match Count: ")[0];
        File fileToOpen = new File(selectedDirectory, fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileToOpen))) {
            StringBuilder fileContent = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by spaces to find individual words
                String[] words = line.split("\\s+");
                
                StringBuilder formattedLine = new StringBuilder();

                for (String word : words) {
                    // Check if the word matches the search term
                    if (word.toLowerCase().contains(searchField.getText().trim().toLowerCase())) {
                        // If the word matches, wrap it with <b> tags for bold styling
                        formattedLine.append("<b>").append(word).append("</b>").append(" ");
                    } else {
                        formattedLine.append(word).append(" ");
                    }
                }

                // Append the formatted line with HTML paragraph tags
                fileContent.append("<p>").append(formattedLine.toString().trim()).append("</p>");
            }

            // Set the HTML content to the JTextPane
            previewTextPane.setContentType("text/html");
            previewTextPane.setText("<html><body>" + fileContent.toString() + "</body></html>");
            previewTextPane.setCaretPosition(0); // Set caret position to the start
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    
    // SEARCH HISTORY FEATURE
    private void showSearchHistory() {
        try {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // show searches
            JLabel label = new JLabel("This session's searches: ");
            panel.add(label);
          
            StringBuilder historyMessage = new StringBuilder();
            for (String term : searchHistory) {
                historyMessage.append(term).append("\n");
            }
            JTextArea historyTextArea = new JTextArea(historyMessage.toString());
            
            // make sure its not editable
            historyTextArea.setEditable(false);
            
            JScrollPane scrollPane = new JScrollPane(historyTextArea);
            panel.add(scrollPane);

            JOptionPane.showMessageDialog(this, panel, "Search History", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
        	// error catch
            JOptionPane.showMessageDialog(this, "Error displaying search history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // CLEAR SEARCH HISTORY
    private void clearSearchHistory() {
        searchHistory.clear();
        statusLabel.setText("History Cleared!");
        statusLabel.setForeground(Color.RED);
    }

    
    // RUNNABLE MAIN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SearchEngineGUI searchEngineGUI = new SearchEngineGUI();
            centerWindow(searchEngineGUI);
        });
    }
    
    
    // CENTER THE WINDOW
    private static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }
}
