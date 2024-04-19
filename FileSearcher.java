/*
 * 
 * 
 * 
 * 
 * 
 * */




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
import javax.swing.JOptionPane;

public class FileSearcher {

   
        public void performSearch(File selectedDirectory, String searchTerm, DefaultListModel<String> resultListModel, JLabel statusLabel) {
            statusLabel.setText("Searching...");
            statusLabel.setForeground(Color.BLUE);
            if (selectedDirectory == null) {
                JOptionPane.showMessageDialog(null, "Please select a Directory.");
                statusLabel.setText(" ");
                return;
            }

            File[] files = selectedDirectory.listFiles();

            List<FileMatch> fileMatches = new ArrayList<>();
            for (File file : files) {
                try {
                    String fileContent = new String(Files.readAllBytes(file.toPath()));
                   
                    if (!searchTerm.contains("*") && !searchTerm.contains("?")) {
                        
               
                        String regex = "\\b" + Pattern.quote(searchTerm) + "\\b";
                        // Check if the file content matches the regex pattern
                        Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(fileContent);
                        int matchCount = 0;
                        while (matcher.find()) {
                            matchCount++;
                        }
                        if (matchCount > 0) {
                            fileMatches.add(new FileMatch(file, matchCount));
                        }
                    } else if (searchTerm.contains("*") || searchTerm.contains("?")) {
                        System.out.println("Wildcard search");
                        // Convert search term to regex pattern
                        String regex = searchTerm
                                .replace(".", "\\.")
                                .replace("?", ".")
                                .replace("*", ".*");
                        // Check if the file content matches the regex pattern
                        if (fileContent.toLowerCase().matches("(?s).*" + regex.toLowerCase() + ".*")) {
                            // If it matches, count occurrences of the search term
                            int matchCount = countOccurrencesUsingRegex(fileContent.toLowerCase(), searchTerm.toLowerCase());
                            if (matchCount > 0) {
                                fileMatches.add(new FileMatch(file, matchCount));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            fileMatches.sort((fm1, fm2) -> Integer.compare(fm2.getMatchCount(), fm1.getMatchCount()));

            resultListModel.clear();

            if (fileMatches.isEmpty()) {
                resultListModel.addElement("No matches found.");
            } else {
                for (FileMatch fileMatch : fileMatches) {
                    String fileName = fileMatch.getFile().getName();
                    int matchCount = fileMatch.getMatchCount();
                    resultListModel.addElement(fileName + " - Match Count: " + matchCount);
                    Logger.log("Match found in file: " + fileName);
                }
            }

            statusLabel.setText("Search Completed!");
            statusLabel.setForeground(Color.GREEN);
        }

        private int countOccurrencesUsingRegex(String input, String searchTerm) {
            Matcher matcher = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE).matcher(input);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            return count;
        }

      
    

    private int countOccurrences(String text, String searchTerm) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(searchTerm, index)) != -1) {
            index += searchTerm.length();
            count++;
        }
        return count;
    }

    public String highlightSearchTerms(String text, String searchTerm) {
        Pattern pattern = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group().toUpperCase());
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    static class Logger {
        public static void log(String message) {
            System.out.println(" - " + message);
        }
    }
}