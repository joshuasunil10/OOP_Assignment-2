import java.io.File;

class FileMatch {
    private File file;
    private int matchCount;

    public FileMatch(File file, double matchStrength) {
        this.file = file;
        this.matchCount = (int) matchStrength;
    }

    public File getFile() {
        return file;
    }

    public int getMatchCount() {
        return matchCount;
    }
}