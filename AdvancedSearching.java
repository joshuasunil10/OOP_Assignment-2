import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvancedSearching implements WildcardSearch {
    @Override
    public void performWildcardSearch(File file, String searchTerm, String fileContent, List<FileMatch> fileMatches) {
        System.out.println("Wildcard search");
        String regex = searchTerm
                .replace(".", "\\.")
                .replace("?", ".")
                .replace("*", ".*");
        if (fileContent.toLowerCase().matches("(?s).*" + regex.toLowerCase() + ".*")) {
            // If it matches, highlight the matching text in bold
            String highlightedContent = highlightSearchTerms(fileContent, searchTerm);
            // If there are any matches, add the file to the result list
            if (!highlightedContent.equals(fileContent)) {
                fileMatches.add(new FileMatch(file, countOccurrencesUsingRegex(highlightedContent, searchTerm)));
            }
        }
    }

    private int countOccurrencesUsingRegex(String input, String searchTerm) {
        Matcher matcher = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE).matcher(input);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private String highlightSearchTerms(String text, String searchTerm) {
        Pattern pattern = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "<b>" + matcher.group() + "</b>");
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}

interface WildcardSearch {
    void performWildcardSearch(File file, String searchTerm, String fileContent, List<FileMatch> fileMatches);
}
