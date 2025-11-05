package com.documentvault.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for FileUtil utility class.
 */
@DisplayName("FileUtil Tests")
class FileUtilTest {

    @TempDir
    Path tempDir;

    // ========== isValidUrl Tests ==========

    @Test
    @DisplayName("Should validate http URL")
    void testValidHttpUrl() {
        assertThat(FileUtil.isValidUrl("http://example.com")).isTrue();
    }

    @Test
    @DisplayName("Should validate https URL")
    void testValidHttpsUrl() {
        assertThat(FileUtil.isValidUrl("https://example.com")).isTrue();
    }

    @Test
    @DisplayName("Should validate URL with path")
    void testValidUrlWithPath() {
        assertThat(FileUtil.isValidUrl("https://example.com/path/to/resource")).isTrue();
    }

    @Test
    @DisplayName("Should validate URL with query parameters")
    void testValidUrlWithQuery() {
        assertThat(FileUtil.isValidUrl("https://example.com?param=value&other=123")).isTrue();
    }

    @Test
    @DisplayName("Should validate URL with port")
    void testValidUrlWithPort() {
        assertThat(FileUtil.isValidUrl("http://example.com:8080/path")).isTrue();
    }

    @Test
    @DisplayName("Should reject URL without protocol")
    void testInvalidUrlNoProtocol() {
        assertThat(FileUtil.isValidUrl("example.com")).isFalse();
    }

    @Test
    @DisplayName("Should reject URL with ftp protocol")
    void testInvalidUrlFtp() {
        assertThat(FileUtil.isValidUrl("ftp://example.com")).isFalse();
    }

    @Test
    @DisplayName("Should reject null URL")
    void testInvalidUrlNull() {
        assertThat(FileUtil.isValidUrl(null)).isFalse();
    }

    @Test
    @DisplayName("Should reject empty URL")
    void testInvalidUrlEmpty() {
        assertThat(FileUtil.isValidUrl("")).isFalse();
    }

    @Test
    @DisplayName("Should reject whitespace-only URL")
    void testInvalidUrlWhitespace() {
        assertThat(FileUtil.isValidUrl("   ")).isFalse();
    }

    @Test
    @DisplayName("Should handle case insensitivity for protocol")
    void testUrlCaseInsensitive() {
        assertThat(FileUtil.isValidUrl("HTTP://example.com")).isTrue();
        assertThat(FileUtil.isValidUrl("HTTPS://example.com")).isTrue();
        assertThat(FileUtil.isValidUrl("HtTp://example.com")).isTrue();
    }

    // ========== fileExists Tests ==========

    @Test
    @DisplayName("Should return true for existing file")
    void testFileExists() throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.createFile(testFile);

        assertThat(FileUtil.fileExists(testFile.toString())).isTrue();
    }

    @Test
    @DisplayName("Should return false for non-existing file")
    void testFileNotExists() {
        String nonExistentPath = tempDir.resolve("nonexistent.txt").toString();

        assertThat(FileUtil.fileExists(nonExistentPath)).isFalse();
    }

    @Test
    @DisplayName("Should return true for existing directory")
    void testDirectoryExists() throws IOException {
        Path testDir = tempDir.resolve("testdir");
        Files.createDirectory(testDir);

        assertThat(FileUtil.fileExists(testDir.toString())).isTrue();
    }

    @Test
    @DisplayName("Should return false for null path")
    void testFileExistsNull() {
        assertThat(FileUtil.fileExists(null)).isFalse();
    }

    @Test
    @DisplayName("Should return false for empty path")
    void testFileExistsEmpty() {
        assertThat(FileUtil.fileExists("")).isFalse();
    }

    @Test
    @DisplayName("Should return false for whitespace path")
    void testFileExistsWhitespace() {
        assertThat(FileUtil.fileExists("   ")).isFalse();
    }

    // ========== getFileSize Tests ==========

    @Test
    @DisplayName("Should return correct file size")
    void testGetFileSize() throws IOException {
        Path testFile = tempDir.resolve("test.txt");
        String content = "Hello, World!";
        Files.writeString(testFile, content);

        long size = FileUtil.getFileSize(testFile.toString());

        assertThat(size).isEqualTo(content.length());
    }

    @Test
    @DisplayName("Should return 0 for empty file")
    void testGetFileSizeEmpty() throws IOException {
        Path testFile = tempDir.resolve("empty.txt");
        Files.createFile(testFile);

        long size = FileUtil.getFileSize(testFile.toString());

        assertThat(size).isZero();
    }

    @Test
    @DisplayName("Should return 0 for non-existing file")
    void testGetFileSizeNonExistent() {
        String nonExistentPath = tempDir.resolve("nonexistent.txt").toString();

        long size = FileUtil.getFileSize(nonExistentPath);

        assertThat(size).isZero();
    }

    @Test
    @DisplayName("Should return 0 for null path")
    void testGetFileSizeNull() {
        long size = FileUtil.getFileSize(null);

        assertThat(size).isZero();
    }

    @Test
    @DisplayName("Should return 0 for empty path")
    void testGetFileSizeEmptyPath() {
        long size = FileUtil.getFileSize("");

        assertThat(size).isZero();
    }

    @Test
    @DisplayName("Should handle large file size")
    void testGetFileSizeLarge() throws IOException {
        Path testFile = tempDir.resolve("large.txt");
        byte[] data = new byte[10000];
        Files.write(testFile, data);

        long size = FileUtil.getFileSize(testFile.toString());

        assertThat(size).isEqualTo(10000);
    }

    // ========== formatFileSize Tests ==========

    @Test
    @DisplayName("Should format bytes")
    void testFormatBytesSmall() {
        assertThat(FileUtil.formatFileSize(0)).isEqualTo("0 B");
        assertThat(FileUtil.formatFileSize(1)).isEqualTo("1 B");
        assertThat(FileUtil.formatFileSize(512)).isEqualTo("512 B");
        assertThat(FileUtil.formatFileSize(1023)).isEqualTo("1023 B");
    }

    @Test
    @DisplayName("Should format kilobytes")
    void testFormatKilobytes() {
        assertThat(FileUtil.formatFileSize(1024)).isEqualTo("1.00 KB");
        assertThat(FileUtil.formatFileSize(2048)).isEqualTo("2.00 KB");
        assertThat(FileUtil.formatFileSize(5120)).isEqualTo("5.00 KB");
        assertThat(FileUtil.formatFileSize(10240)).isEqualTo("10.00 KB");
    }

    @Test
    @DisplayName("Should format megabytes")
    void testFormatMegabytes() {
        assertThat(FileUtil.formatFileSize(1024 * 1024)).isEqualTo("1.00 MB");
        assertThat(FileUtil.formatFileSize(5 * 1024 * 1024)).isEqualTo("5.00 MB");
        assertThat(FileUtil.formatFileSize(10 * 1024 * 1024)).isEqualTo("10.00 MB");
    }

    @Test
    @DisplayName("Should format gigabytes")
    void testFormatGigabytes() {
        assertThat(FileUtil.formatFileSize(1024L * 1024 * 1024)).isEqualTo("1.00 GB");
        assertThat(FileUtil.formatFileSize(5L * 1024 * 1024 * 1024)).isEqualTo("5.00 GB");
    }

    @Test
    @DisplayName("Should format with decimal precision")
    void testFormatWithDecimals() {
        assertThat(FileUtil.formatFileSize(1536)).isEqualTo("1.50 KB");
        assertThat(FileUtil.formatFileSize(2560)).isEqualTo("2.50 KB");
        assertThat(FileUtil.formatFileSize(1024 * 1024 + 512 * 1024)).isEqualTo("1.50 MB");
    }

    @Test
    @DisplayName("Should handle boundary values")
    void testFormatBoundaryValues() {
        // Just below 1 KB
        assertThat(FileUtil.formatFileSize(1023)).isEqualTo("1023 B");

        // Exactly 1 KB
        assertThat(FileUtil.formatFileSize(1024)).isEqualTo("1.00 KB");

        // Just below 1 MB
        assertThat(FileUtil.formatFileSize(1024 * 1024 - 1)).matches("\\d+\\.\\d{2} KB");

        // Exactly 1 MB
        assertThat(FileUtil.formatFileSize(1024 * 1024)).isEqualTo("1.00 MB");
    }

    // ========== openFile Tests (Limited - requires Desktop support) ==========

    @Test
    @DisplayName("Should return false for null file path in openFile")
    void testOpenFileNull() {
        assertThat(FileUtil.openFile(null)).isFalse();
    }

    @Test
    @DisplayName("Should return false for empty file path in openFile")
    void testOpenFileEmpty() {
        assertThat(FileUtil.openFile("")).isFalse();
    }

    @Test
    @DisplayName("Should return false for whitespace file path in openFile")
    void testOpenFileWhitespace() {
        assertThat(FileUtil.openFile("   ")).isFalse();
    }

    @Test
    @DisplayName("Should return false for non-existing file in openFile")
    void testOpenFileNonExistent() {
        String nonExistentPath = tempDir.resolve("nonexistent.txt").toString();

        assertThat(FileUtil.openFile(nonExistentPath)).isFalse();
    }

    // ========== openUrl Tests (Limited - requires Desktop support) ==========

    @Test
    @DisplayName("Should return false for null URL in openUrl")
    void testOpenUrlNull() {
        assertThat(FileUtil.openUrl(null)).isFalse();
    }

    @Test
    @DisplayName("Should return false for empty URL in openUrl")
    void testOpenUrlEmpty() {
        assertThat(FileUtil.openUrl("")).isFalse();
    }

    @Test
    @DisplayName("Should return false for whitespace URL in openUrl")
    void testOpenUrlWhitespace() {
        assertThat(FileUtil.openUrl("   ")).isFalse();
    }

    // ========== Edge Cases and Integration ==========

    @Test
    @DisplayName("Should handle very long file paths")
    void testVeryLongPath() {
        String longPath = "a".repeat(500);

        assertThat(FileUtil.fileExists(longPath)).isFalse();
        assertThat(FileUtil.getFileSize(longPath)).isZero();
    }

    @Test
    @DisplayName("Should handle special characters in file path")
    void testSpecialCharactersInPath() throws IOException {
        Path testFile = tempDir.resolve("test-file_2025.txt");
        Files.createFile(testFile);

        assertThat(FileUtil.fileExists(testFile.toString())).isTrue();
        assertThat(FileUtil.getFileSize(testFile.toString())).isZero();
    }

    @Test
    @DisplayName("Should format very large file sizes")
    void testVeryLargeFileSize() {
        long terabyte = 1024L * 1024 * 1024 * 1024;
        String formatted = FileUtil.formatFileSize(terabyte);

        assertThat(formatted).isEqualTo("1024.00 GB");
    }

    @Test
    @DisplayName("Should handle URL with fragment")
    void testUrlWithFragment() {
        assertThat(FileUtil.isValidUrl("https://example.com/page#section")).isTrue();
    }

    @Test
    @DisplayName("Should handle URL with username and password")
    void testUrlWithCredentials() {
        assertThat(FileUtil.isValidUrl("https://user:pass@example.com/path")).isTrue();
    }

    @Test
    @DisplayName("Should handle localhost URLs")
    void testLocalhostUrl() {
        assertThat(FileUtil.isValidUrl("http://localhost:8080")).isTrue();
        assertThat(FileUtil.isValidUrl("http://127.0.0.1:3000")).isTrue();
    }

    @Test
    @DisplayName("Should handle file size calculation for directory")
    void testFileSizeForDirectory() throws IOException {
        Path testDir = tempDir.resolve("testdir");
        Files.createDirectory(testDir);

        // Getting size of directory should return 0 or handle gracefully
        long size = FileUtil.getFileSize(testDir.toString());

        // Directory size handling varies, just ensure no exception
        assertThat(size).isGreaterThanOrEqualTo(0);
    }
}
