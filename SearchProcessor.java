/* OBJECT ORIENTED PROGRAMMING ASSIGNMENT 
 * 
 * AUTHOR : JOSHUA SUNIL MATHEW C22419706
 * 
 * SearchProcessor - this is the main searching class, and contains all the logic for handling searching, and passing over to advanced searching, 
 * the results are also ranked using the match count logic within this class
 * */




// imports
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;

public class SearchProcessor {
	
	// main searching method
    public void performSearch(File selectedDirectory, String searchTerm, DefaultListModel<String> resultListModel, JLabel statusLabel) {
    	// update status label
        statusLabel.setText("Searching...");
        statusLabel.setForeground(Color.BLUE);
        
        // getting a list of files in the directory
        File[] files = selectedDirectory.listFiles();

        List<FileMatch> fileMatches = new ArrayList<>();
        for (File file : files) {
            try {
                String fileContent = new String(Files.readAllBytes(file.toPath()));
                
                
               // if the search term does not contain any wildcards
                if (!searchTerm.contains("*") && !searchTerm.contains("?")) {
                    String regex = "\\b" + Pattern.quote(searchTerm) + "\\b";
                    Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(fileContent);
                    int matchCount = 0;
                    while (matcher.find()) {
                        matchCount++; // count the amount of matches
                    }
                    if (matchCount > 0) {
                        fileMatches.add(new FileMatch(file, matchCount));
                    }
                } else if (searchTerm.contains("*") || searchTerm.contains("?")) {
                    // create a new advanced searching instance
                    AdvancedSearching wildcardSearcher = new AdvancedSearching();
                    wildcardSearcher.performWildcardSearch(file, searchTerm, fileContent, fileMatches);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // rank based on match count
        fileMatches.sort((fm1, fm2) -> Integer.compare(fm2.getMatchCount(), fm1.getMatchCount()));

        resultListModel.clear(); // clear previous search results

        if (fileMatches.isEmpty()) {
        	// if no matches
            resultListModel.addElement("No matches found.");
        } else {
            for (FileMatch fileMatch : fileMatches) {
                String fileName = fileMatch.getFile().getName();
                int matchCount = fileMatch.getMatchCount();
                // display in results table
                resultListModel.addElement(fileName + " - Match Count: " + matchCount);
                
                // log processes
                Logger.log("Match found in file: " + fileName);
            }
        }
        
        // change status label
        statusLabel.setText("Search Completed!");
        statusLabel.setForeground(Color.GREEN);
    }
    
    
    // Logger Class to implement console loging for debugging and showing processes
    private static class Logger {
        public static void log(String message) {
            System.out.println(" - " + message);
        }
    }
}
