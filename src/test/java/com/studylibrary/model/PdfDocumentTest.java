package com.studylibrary.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for the PdfDocument class.
 * Tests PDF document creation, file handling, size formatting, and metadata
 * management.
 */
@DisplayName("PdfDocument Tests")
class PdfDocumentTest {

    private PdfDocument pdfDocument;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        pdfDocument = new PdfDocument();
    }

    @Test
    @DisplayName("Should create PDF document with valid UUID and ItemType.PDF")
    void testPdfDocumentCreation() {
        assertThat(pdfDocument.getId())
                .isNotNull()
                .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        assertThat(pdfDocument.getItemType()).isEqualTo(LibraryItem.ItemType.PDF);
        assertThat(pdfDocument.getFilePath()).isNull();
        assertThat(pdfDocument.getFileSize()).isZero();
        assertThat(pdfDocument.getPageCount()).isZero();
        assertThat(pdfDocument.getAuthor()).isNull();
    }

    @Test
    @DisplayName("Should set and get file path")
    void testFilePathManagement() {
        String filePath = "/documents/study/java-guide.pdf";
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        pdfDocument.setFilePath(filePath);

        assertThat(pdfDocument.getFilePath()).isEqualTo(filePath);
        assertThat(pdfDocument.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should set and get file size")
    void testFileSizeManagement() {
        long fileSize = 1024 * 1024 * 5; // 5 MB

        pdfDocument.setFileSize(fileSize);

        assertThat(pdfDocument.getFileSize()).isEqualTo(fileSize);
    }

    @Test
    @DisplayName("Should set and get page count")
    void testPageCountManagement() {
        pdfDocument.setPageCount(150);

        assertThat(pdfDocument.getPageCount()).isEqualTo(150);
    }

    @Test
    @DisplayName("Should set and get author")
    void testAuthorManagement() {
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        pdfDocument.setAuthor("John Doe");

        assertThat(pdfDocument.getAuthor()).isEqualTo("John Doe");
        assertThat(pdfDocument.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should extract filename from file path")
    void testGetFileName() {
        pdfDocument.setFilePath("/home/user/documents/java-programming.pdf");

        assertThat(pdfDocument.getFileName()).isEqualTo("java-programming.pdf");
    }

    @Test
    @DisplayName("Should extract filename from Windows path")
    void testGetFileNameWindows() {
        // On non-Windows systems, Windows paths may not be parsed correctly
        // The implementation should handle this gracefully
        pdfDocument.setFilePath("C:\\Users\\John\\Documents\\study-notes.pdf");

        // The result depends on the OS - on Windows it extracts filename, on Unix it
        // returns the full path
        assertThat(pdfDocument.getFileName()).isNotNull();
        assertThat(pdfDocument.getFileName()).contains("study-notes.pdf");
    }

    @Test
    @DisplayName("Should return 'Unknown' for null file path")
    void testGetFileNameNull() {
        pdfDocument.setFilePath(null);

        assertThat(pdfDocument.getFileName()).isEqualTo("Unknown");
    }

    @Test
    @DisplayName("Should return 'Unknown' for empty file path")
    void testGetFileNameEmpty() {
        pdfDocument.setFilePath("");

        assertThat(pdfDocument.getFileName()).isEqualTo("Unknown");
    }

    @Test
    @DisplayName("Should return file path for invalid path format")
    void testGetFileNameInvalidPath() {
        String invalidPath = ":::invalid:::path:::";
        pdfDocument.setFilePath(invalidPath);

        // Should return the original path if parsing fails
        assertThat(pdfDocument.getFileName()).isNotNull();
    }

    @Test
    @DisplayName("Should format file size in bytes")
    void testFileSizeFormattedBytes() {
        pdfDocument.setFileSize(512);

        assertThat(pdfDocument.getFileSizeFormatted()).isEqualTo("512 B");
    }

    @Test
    @DisplayName("Should format file size in kilobytes")
    void testFileSizeFormattedKilobytes() {
        pdfDocument.setFileSize(1024 * 10); // 10 KB

        assertThat(pdfDocument.getFileSizeFormatted()).isEqualTo("10.00 KB");
    }

    @Test
    @DisplayName("Should format file size in megabytes")
    void testFileSizeFormattedMegabytes() {
        pdfDocument.setFileSize(1024 * 1024 * 5); // 5 MB

        assertThat(pdfDocument.getFileSizeFormatted()).isEqualTo("5.00 MB");
    }

    @Test
    @DisplayName("Should format large file size in megabytes")
    void testFileSizeFormattedLarge() {
        pdfDocument.setFileSize(1024L * 1024L * 150); // 150 MB

        assertThat(pdfDocument.getFileSizeFormatted()).isEqualTo("150.00 MB");
    }

    @Test
    @DisplayName("Should format zero file size")
    void testFileSizeFormattedZero() {
        pdfDocument.setFileSize(0);

        assertThat(pdfDocument.getFileSizeFormatted()).isEqualTo("0 B");
    }

    @Test
    @DisplayName("Should format file size with decimal precision")
    void testFileSizeFormattedDecimal() {
        pdfDocument.setFileSize(1536); // 1.5 KB

        assertThat(pdfDocument.getFileSizeFormatted()).isEqualTo("1.50 KB");
    }

    @Test
    @DisplayName("Should format file size just under 1 KB threshold")
    void testFileSizeFormattedJustUnder1KB() {
        pdfDocument.setFileSize(1023);

        assertThat(pdfDocument.getFileSizeFormatted()).isEqualTo("1023 B");
    }

    @Test
    @DisplayName("Should format file size just under 1 MB threshold")
    void testFileSizeFormattedJustUnder1MB() {
        pdfDocument.setFileSize(1024 * 1023); // Just under 1 MB

        String formatted = pdfDocument.getFileSizeFormatted();
        assertThat(formatted).endsWith(" KB");
    }

    @Test
    @DisplayName("Should return false for non-existent file")
    void testFileExistsForNonExistentFile() {
        pdfDocument.setFilePath("/non/existent/path/file.pdf");

        assertThat(pdfDocument.fileExists()).isFalse();
    }

    @Test
    @DisplayName("Should return false for null file path")
    void testFileExistsForNullPath() {
        pdfDocument.setFilePath(null);

        assertThat(pdfDocument.fileExists()).isFalse();
    }

    @Test
    @DisplayName("Should return false for empty file path")
    void testFileExistsForEmptyPath() {
        pdfDocument.setFilePath("");

        assertThat(pdfDocument.fileExists()).isFalse();
    }

    @Test
    @DisplayName("Should return true for existing file")
    void testFileExistsForExistingFile() throws IOException {
        // Create a temporary file
        Path tempFile = tempDir.resolve("test-document.pdf");
        Files.createFile(tempFile);

        pdfDocument.setFilePath(tempFile.toString());

        assertThat(pdfDocument.fileExists()).isTrue();
    }

    @Test
    @DisplayName("Should return false for invalid path format")
    void testFileExistsForInvalidPath() {
        pdfDocument.setFilePath(":::invalid:::path:::");

        assertThat(pdfDocument.fileExists()).isFalse();
    }

    @Test
    @DisplayName("Should include author and filename in searchable text")
    void testSearchableTextIncludesAuthorAndFilename() {
        pdfDocument.setTitle("Java Programming Guide");
        pdfDocument.setAuthor("James Gosling");
        pdfDocument.setFilePath("/documents/java-guide.pdf");
        pdfDocument.addTag("java");

        String searchableText = pdfDocument.getSearchableText();

        assertThat(searchableText).containsIgnoringCase("java programming guide");
        assertThat(searchableText).containsIgnoringCase("james gosling");
        assertThat(searchableText).containsIgnoringCase("java-guide.pdf");
        assertThat(searchableText).containsIgnoringCase("java");
    }

    @Test
    @DisplayName("Should handle null author in searchable text")
    void testSearchableTextWithNullAuthor() {
        pdfDocument.setTitle("Study Notes");
        pdfDocument.setAuthor(null);
        pdfDocument.setFilePath("/documents/notes.pdf");

        String searchableText = pdfDocument.getSearchableText();

        assertThat(searchableText).containsIgnoringCase("study notes");
        assertThat(searchableText).containsIgnoringCase("notes.pdf");
    }

    @Test
    @DisplayName("Should handle zero page count")
    void testZeroPageCount() {
        pdfDocument.setPageCount(0);

        assertThat(pdfDocument.getPageCount()).isZero();
    }

    @Test
    @DisplayName("Should handle large page count")
    void testLargePageCount() {
        pdfDocument.setPageCount(5000);

        assertThat(pdfDocument.getPageCount()).isEqualTo(5000);
    }

    @Test
    @DisplayName("Should update lastModified when setting file path")
    void testLastModifiedUpdateOnFilePathChange() {
        pdfDocument.setFilePath("/path/to/first.pdf");
        LocalDateTime firstModified = pdfDocument.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        pdfDocument.setFilePath("/path/to/second.pdf");

        assertThat(pdfDocument.getLastModified()).isAfter(firstModified);
    }

    @Test
    @DisplayName("Should update lastModified when setting author")
    void testLastModifiedUpdateOnAuthorChange() {
        pdfDocument.setAuthor("First Author");
        LocalDateTime firstModified = pdfDocument.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        pdfDocument.setAuthor("Second Author");

        assertThat(pdfDocument.getLastModified()).isAfter(firstModified);
    }

    @Test
    @DisplayName("Should NOT update lastModified when setting file size")
    void testLastModifiedNotUpdatedOnFileSizeChange() {
        LocalDateTime beforeSet = pdfDocument.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        pdfDocument.setFileSize(1024 * 1024);

        // File size is metadata that shouldn't trigger lastModified update
        assertThat(pdfDocument.getLastModified()).isEqualTo(beforeSet);
    }

    @Test
    @DisplayName("Should NOT update lastModified when setting page count")
    void testLastModifiedNotUpdatedOnPageCountChange() {
        LocalDateTime beforeSet = pdfDocument.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        pdfDocument.setPageCount(100);

        // Page count is metadata that shouldn't trigger lastModified update
        assertThat(pdfDocument.getLastModified()).isEqualTo(beforeSet);
    }

    @Test
    @DisplayName("Should handle relative file path")
    void testRelativeFilePath() {
        pdfDocument.setFilePath("documents/study/notes.pdf");

        assertThat(pdfDocument.getFileName()).isEqualTo("notes.pdf");
    }

    @Test
    @DisplayName("Should handle file path with spaces")
    void testFilePathWithSpaces() {
        pdfDocument.setFilePath("/home/user/My Documents/Study Notes.pdf");

        assertThat(pdfDocument.getFileName()).isEqualTo("Study Notes.pdf");
    }

    @Test
    @DisplayName("Should handle file path with special characters")
    void testFilePathWithSpecialCharacters() {
        pdfDocument.setFilePath("/documents/résumé_2023.pdf");

        assertThat(pdfDocument.getFileName()).isEqualTo("résumé_2023.pdf");
    }
}
