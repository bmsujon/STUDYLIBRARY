package com.documentvault.model;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents a PDF Document item in the study library.
 * Stores the file path and metadata about the PDF.
 * Java 25: Final class for sealed hierarchy.
 */
public final class PdfDocument extends LibraryItem {

    private String filePath;
    private long fileSize; // in bytes
    private int pageCount;
    private String author;

    public PdfDocument() {
        super(ItemType.PDF);
        this.pageCount = 0;
        this.fileSize = 0;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        touch();
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
        touch();
    }

    /**
     * Returns the filename from the file path.
     */
    public String getFileName() {
        if (filePath == null || filePath.isEmpty()) {
            return "Unknown";
        }
        try {
            Path path = Paths.get(filePath);
            return path.getFileName().toString();
        } catch (Exception e) {
            return filePath;
        }
    }

    /**
     * Returns a human-readable file size.
     */
    public String getFileSizeFormatted() {
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.2f KB", fileSize / 1024.0);
        } else {
            return String.format("%.2f MB", fileSize / (1024.0 * 1024.0));
        }
    }

    /**
     * Checks if the file exists at the specified path.
     */
    public boolean fileExists() {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        try {
            return Paths.get(filePath).toFile().exists();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getSearchableText() {
        return super.getSearchableText() + " " +
                (author != null ? author : "") + " " +
                getFileName();
    }
}
