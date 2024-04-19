/* OBJECT ORIENTED PROGRAMMING ASSIGNMENT 
 * 
 * AUTHOR : JOSHUA SUNIL MATHEW C22419706
 * 
 * File Match - shows a matched file, and its corresponding strength of that match
 * 
 * */

// imports
import java.io.File;

// representation of a matched file
class FileMatch {
	// the file itself
    private File file;
    // strength
    private int matchCount;

    // constructor
    public FileMatch(File file, double matchStrength) {
        this.file = file;
        this.matchCount = (int) matchStrength;
    }

    // getter to retrieve the file
    public File getFile() {
        return file;
    }

    // getter to get match strength
    public int getMatchCount() {
        return matchCount;
    }
}
