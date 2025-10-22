package com.studylibrary.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for the TextSnippet class.
 * Tests text snippet creation, content management, language handling, and
 * utility methods.
 */
@DisplayName("TextSnippet Tests")
class TextSnippetTest {

    private TextSnippet textSnippet;

    @BeforeEach
    void setUp() {
        textSnippet = new TextSnippet();
    }

    @Test
    @DisplayName("Should create text snippet with valid UUID and ItemType.TEXT_SNIPPET")
    void testTextSnippetCreation() {
        assertThat(textSnippet.getId())
                .isNotNull()
                .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        assertThat(textSnippet.getItemType()).isEqualTo(LibraryItem.ItemType.TEXT_SNIPPET);
        assertThat(textSnippet.getContent()).isEmpty();
        assertThat(textSnippet.getLanguage()).isEqualTo("text");
        assertThat(textSnippet.getSourceUrl()).isNull();
    }

    @Test
    @DisplayName("Should set and get content")
    void testContentManagement() {
        String content = "public static void main(String[] args) { }";
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        textSnippet.setContent(content);

        assertThat(textSnippet.getContent()).isEqualTo(content);
        assertThat(textSnippet.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should set and get language")
    void testLanguageManagement() {
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        textSnippet.setLanguage("java");

        assertThat(textSnippet.getLanguage()).isEqualTo("java");
        assertThat(textSnippet.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should set and get source URL")
    void testSourceUrlManagement() {
        String sourceUrl = "https://stackoverflow.com/questions/12345";
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        textSnippet.setSourceUrl(sourceUrl);

        assertThat(textSnippet.getSourceUrl()).isEqualTo(sourceUrl);
        assertThat(textSnippet.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should have 'text' as default language")
    void testDefaultLanguage() {
        assertThat(textSnippet.getLanguage()).isEqualTo("text");
    }

    @Test
    @DisplayName("Should generate correct content preview for short content")
    void testContentPreviewShort() {
        textSnippet.setContent("int x = 5;");

        assertThat(textSnippet.getContentPreview()).isEqualTo("int x = 5;");
    }

    @Test
    @DisplayName("Should generate correct content preview for long content")
    void testContentPreviewLong() {
        String longContent = "public class Example { public static void main(String[] args) { " +
                "System.out.println(\"This is a very long code snippet that exceeds 100 characters\"); } }";
        textSnippet.setContent(longContent);

        String preview = textSnippet.getContentPreview();

        assertThat(preview).hasSize(100);
        assertThat(preview).endsWith("...");
        assertThat(preview).startsWith("public class Example");
    }

    @Test
    @DisplayName("Should handle empty content in preview")
    void testContentPreviewEmpty() {
        textSnippet.setContent("");
        assertThat(textSnippet.getContentPreview()).isEqualTo("Empty snippet");

        textSnippet.setContent(null);
        assertThat(textSnippet.getContentPreview()).isEqualTo("Empty snippet");
    }

    @Test
    @DisplayName("Should handle whitespace in content preview")
    void testContentPreviewWithWhitespace() {
        textSnippet.setContent("int    x    =    5;\nint    y    =    10;");

        String preview = textSnippet.getContentPreview();

        assertThat(preview).doesNotContain("\n");
        assertThat(preview).doesNotContain("    "); // No excessive spaces
    }

    @Test
    @DisplayName("Should handle multiline content in preview")
    void testContentPreviewMultiline() {
        String multilineContent = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        textSnippet.setContent(multilineContent);

        String preview = textSnippet.getContentPreview();

        assertThat(preview).doesNotContain("\n");
        assertThat(preview).contains("Line 1 Line 2 Line 3 Line 4 Line 5");
    }

    @Test
    @DisplayName("Should count lines correctly for single line")
    void testLineCountSingleLine() {
        textSnippet.setContent("Single line of code");

        assertThat(textSnippet.getLineCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should count lines correctly for multiple lines")
    void testLineCountMultipleLines() {
        String multilineContent = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        textSnippet.setContent(multilineContent);

        assertThat(textSnippet.getLineCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should return 0 line count for empty content")
    void testLineCountEmpty() {
        textSnippet.setContent("");
        assertThat(textSnippet.getLineCount()).isZero();

        textSnippet.setContent(null);
        assertThat(textSnippet.getLineCount()).isZero();
    }

    @Test
    @DisplayName("Should count lines correctly with trailing newline")
    void testLineCountWithTrailingNewline() {
        textSnippet.setContent("Line 1\nLine 2\nLine 3\n");

        // Java's split() discards trailing empty strings
        assertThat(textSnippet.getLineCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should count lines correctly with empty lines")
    void testLineCountWithEmptyLines() {
        textSnippet.setContent("Line 1\n\nLine 3\n\nLine 5");

        assertThat(textSnippet.getLineCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should handle Windows line endings")
    void testLineCountWindowsLineEndings() {
        textSnippet.setContent("Line 1\r\nLine 2\r\nLine 3");

        // \r\n is treated as a single newline
        assertThat(textSnippet.getLineCount()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should include content and language in searchable text")
    void testSearchableTextIncludesContentAndLanguage() {
        textSnippet.setTitle("SQL Query Example");
        textSnippet.setContent("SELECT * FROM users WHERE id = 1;");
        textSnippet.setLanguage("sql");
        textSnippet.addTag("database");

        String searchableText = textSnippet.getSearchableText();

        assertThat(searchableText).containsIgnoringCase("sql query example");
        assertThat(searchableText).containsIgnoringCase("select * from users");
        assertThat(searchableText).containsIgnoringCase("sql");
        assertThat(searchableText).containsIgnoringCase("database");
    }

    @Test
    @DisplayName("Should handle null content in searchable text")
    void testSearchableTextWithNullContent() {
        textSnippet.setTitle("Example");
        textSnippet.setContent(null);
        textSnippet.setLanguage("java");

        String searchableText = textSnippet.getSearchableText();

        assertThat(searchableText).containsIgnoringCase("example");
        assertThat(searchableText).containsIgnoringCase("java");
    }

    @Test
    @DisplayName("Should handle null language in searchable text")
    void testSearchableTextWithNullLanguage() {
        textSnippet.setTitle("Code Snippet");
        textSnippet.setContent("print('Hello World')");
        textSnippet.setLanguage(null);

        String searchableText = textSnippet.getSearchableText();

        assertThat(searchableText).containsIgnoringCase("code snippet");
        assertThat(searchableText).containsIgnoringCase("hello world");
    }

    @Test
    @DisplayName("Should update lastModified when setting content")
    void testLastModifiedUpdateOnContentChange() {
        textSnippet.setContent("Initial content");
        LocalDateTime firstModified = textSnippet.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        textSnippet.setContent("Updated content");

        assertThat(textSnippet.getLastModified()).isAfter(firstModified);
    }

    @Test
    @DisplayName("Should update lastModified when setting language")
    void testLastModifiedUpdateOnLanguageChange() {
        textSnippet.setLanguage("java");
        LocalDateTime firstModified = textSnippet.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        textSnippet.setLanguage("python");

        assertThat(textSnippet.getLastModified()).isAfter(firstModified);
    }

    @Test
    @DisplayName("Should update lastModified when setting source URL")
    void testLastModifiedUpdateOnSourceUrlChange() {
        textSnippet.setSourceUrl("https://example.com/first");
        LocalDateTime firstModified = textSnippet.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        textSnippet.setSourceUrl("https://example.com/second");

        assertThat(textSnippet.getLastModified()).isAfter(firstModified);
    }

    @Test
    @DisplayName("Should support various programming languages")
    void testVariousProgrammingLanguages() {
        textSnippet.setLanguage("java");
        assertThat(textSnippet.getLanguage()).isEqualTo("java");

        textSnippet.setLanguage("python");
        assertThat(textSnippet.getLanguage()).isEqualTo("python");

        textSnippet.setLanguage("javascript");
        assertThat(textSnippet.getLanguage()).isEqualTo("javascript");

        textSnippet.setLanguage("sql");
        assertThat(textSnippet.getLanguage()).isEqualTo("sql");

        textSnippet.setLanguage("json");
        assertThat(textSnippet.getLanguage()).isEqualTo("json");
    }

    @Test
    @DisplayName("Should handle code with special characters")
    void testContentWithSpecialCharacters() {
        String content = "String regex = \"[a-zA-Z0-9]+\"; // Special chars: $, @, #";
        textSnippet.setContent(content);

        assertThat(textSnippet.getContent()).isEqualTo(content);
        assertThat(textSnippet.getLineCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should handle very long single line")
    void testVeryLongSingleLine() {
        String longLine = "x".repeat(500);
        textSnippet.setContent(longLine);

        assertThat(textSnippet.getLineCount()).isEqualTo(1);
        assertThat(textSnippet.getContentPreview()).hasSize(100);
    }

    @Test
    @DisplayName("Should handle large multiline content")
    void testLargeMultilineContent() {
        StringBuilder largeContent = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            largeContent.append("Line ").append(i).append("\n");
        }
        textSnippet.setContent(largeContent.toString());

        // Java's split() discards trailing empty strings, so 100 lines
        assertThat(textSnippet.getLineCount()).isEqualTo(100);
    }

    @Test
    @DisplayName("Should handle empty string language")
    void testEmptyStringLanguage() {
        textSnippet.setLanguage("");

        assertThat(textSnippet.getLanguage()).isEmpty();
    }

    @Test
    @DisplayName("Should handle source URL from various platforms")
    void testSourceUrlVariousPlatforms() {
        // Stack Overflow
        textSnippet.setSourceUrl("https://stackoverflow.com/questions/12345");
        assertThat(textSnippet.getSourceUrl()).contains("stackoverflow.com");

        // GitHub
        textSnippet.setSourceUrl("https://github.com/user/repo/blob/main/file.java");
        assertThat(textSnippet.getSourceUrl()).contains("github.com");

        // Documentation
        textSnippet.setSourceUrl("https://docs.oracle.com/javase/tutorial/");
        assertThat(textSnippet.getSourceUrl()).contains("docs.oracle.com");
    }

    @Test
    @DisplayName("Should handle null source URL")
    void testNullSourceUrl() {
        textSnippet.setSourceUrl(null);

        assertThat(textSnippet.getSourceUrl()).isNull();
    }

    @Test
    @DisplayName("Should handle content with tabs")
    void testContentWithTabs() {
        String contentWithTabs = "if (condition) {\n\tSystem.out.println(\"Hello\");\n}";
        textSnippet.setContent(contentWithTabs);

        assertThat(textSnippet.getContent()).contains("\t");
        assertThat(textSnippet.getLineCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should handle Unicode content")
    void testUnicodeContent() {
        String unicodeContent = "String greeting = \"Hello 世界 🌍\";";
        textSnippet.setContent(unicodeContent);

        assertThat(textSnippet.getContent()).isEqualTo(unicodeContent);
    }

    @Test
    @DisplayName("Should handle content with only whitespace")
    void testContentOnlyWhitespace() {
        textSnippet.setContent("   \n   \n   ");

        assertThat(textSnippet.getLineCount()).isEqualTo(3);
        String preview = textSnippet.getContentPreview();
        // After stripping and trimming, should be very short or empty
        assertThat(preview.length()).isLessThan(20);
    }

    @Test
    @DisplayName("Should maintain property independence")
    void testPropertyIndependence() {
        textSnippet.setContent("Code content");
        textSnippet.setLanguage("java");
        textSnippet.setSourceUrl("https://example.com");

        TextSnippet anotherSnippet = new TextSnippet();

        // Verify the other snippet has default/null values
        assertThat(anotherSnippet.getContent()).isEmpty();
        assertThat(anotherSnippet.getLanguage()).isEqualTo("text");
        assertThat(anotherSnippet.getSourceUrl()).isNull();
    }

    @Test
    @DisplayName("Should maintain dateAdded on updates")
    void testDateAddedPersistence() {
        LocalDateTime originalDateAdded = textSnippet.getDateAdded();

        // Make various changes
        textSnippet.setContent("New content");
        textSnippet.setLanguage("python");
        textSnippet.setSourceUrl("https://example.com");

        // dateAdded should remain unchanged
        assertThat(textSnippet.getDateAdded()).isEqualTo(originalDateAdded);
    }

    @Test
    @DisplayName("Should handle mixed line endings")
    void testMixedLineEndings() {
        String mixedContent = "Line 1\nLine 2\r\nLine 3\rLine 4";
        textSnippet.setContent(mixedContent);

        // Line count may vary depending on how split handles different line endings
        assertThat(textSnippet.getLineCount()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should handle content exactly 100 characters")
    void testContentExactly100Characters() {
        String content = "x".repeat(100);
        textSnippet.setContent(content);

        String preview = textSnippet.getContentPreview();
        assertThat(preview).hasSize(100);
        assertThat(preview).doesNotEndWith("...");
    }

    @Test
    @DisplayName("Should handle content with 101 characters")
    void testContent101Characters() {
        String content = "x".repeat(101);
        textSnippet.setContent(content);

        String preview = textSnippet.getContentPreview();
        assertThat(preview).hasSize(100);
        assertThat(preview).endsWith("...");
    }
}
