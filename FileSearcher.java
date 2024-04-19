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
                    Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(fileContent);
                    int matchCount = 0;
                    while (matcher.find()) {
                        matchCount++;
                    }
                    if (matchCount > 0) {
                        fileMatches.add(new FileMatch(file, matchCount));
                    }
                } else if (searchTerm.contains("*") || searchTerm.contains("?")) {
                    // Delegate wildcard search to AdvancedSearching class
                    AdvancedSearching wildcardSearcher = new AdvancedSearching();
                    wildcardSearcher.performWildcardSearch(file, searchTerm, fileContent, fileMatches);
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

    private static class Logger {
        public static void log(String message) {
            System.out.println(" - " + message);
        }
    }
}
