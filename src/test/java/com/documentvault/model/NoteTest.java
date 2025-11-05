package com.documentvault.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for the Note class.
 * Tests note creation, content management, markdown support, and search
 * functionality.
 */
@DisplayName("Note Tests")
class NoteTest {

    private Note note;

    @BeforeEach
    void setUp() {
        note = new Note();
    }

    @Test
    @DisplayName("Should create note with valid UUID and ItemType.NOTE")
    void testNoteCreation() {
        assertThat(note.getId())
                .isNotNull()
                .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        assertThat(note.getItemType()).isEqualTo(LibraryItem.ItemType.NOTE);
        assertThat(note.getContent()).isEmpty();
        assertThat(note.isMarkdown()).isFalse();
    }

    @Test
    @DisplayName("Should set and get content")
    void testContentManagement() {
        String content = "This is a test note content with important information.";
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        note.setContent(content);

        assertThat(note.getContent()).isEqualTo(content);
        assertThat(note.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should set and get markdown flag")
    void testMarkdownFlag() {
        assertThat(note.isMarkdown()).isFalse();

        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);
        note.setMarkdown(true);

        assertThat(note.isMarkdown()).isTrue();
        assertThat(note.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should generate correct content preview for short content")
    void testContentPreviewShort() {
        note.setContent("Short note");

        assertThat(note.getContentPreview()).isEqualTo("Short note");
    }

    @Test
    @DisplayName("Should generate correct content preview for long content")
    void testContentPreviewLong() {
        String longContent = "This is a very long note content that exceeds the 100 character limit " +
                "and should be truncated with ellipsis at the end of the preview text.";
        note.setContent(longContent);

        String preview = note.getContentPreview();

        assertThat(preview).hasSize(100);
        assertThat(preview).endsWith("...");
        assertThat(preview).startsWith("This is a very long note content");
    }

    @Test
    @DisplayName("Should handle empty content in preview")
    void testContentPreviewEmpty() {
        note.setContent("");
        assertThat(note.getContentPreview()).isEqualTo("Empty note");

        note.setContent(null);
        assertThat(note.getContentPreview()).isEqualTo("Empty note");
    }

    @Test
    @DisplayName("Should handle whitespace in content preview")
    void testContentPreviewWithWhitespace() {
        note.setContent("Content   with    multiple     spaces\nand\nnewlines");

        String preview = note.getContentPreview();

        assertThat(preview).doesNotContain("\n");
        assertThat(preview).doesNotContain("  "); // No double spaces
        assertThat(preview).isEqualTo("Content with multiple spaces and newlines");
    }

    @Test
    @DisplayName("Should include content in searchable text")
    void testSearchableTextIncludesContent() {
        note.setTitle("Study Notes");
        note.setContent("Java programming concepts and patterns");
        note.addTag("java");

        String searchableText = note.getSearchableText();

        assertThat(searchableText).containsIgnoringCase("study notes");
        assertThat(searchableText).containsIgnoringCase("java programming");
        assertThat(searchableText).containsIgnoringCase("java");
    }

    @Test
    @DisplayName("Should update lastModified when setting content")
    void testLastModifiedUpdateOnContentChange() {
        note.setContent("Initial content");
        LocalDateTime firstModified = note.getLastModified();

        // Wait a tiny bit to ensure timestamp difference
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        note.setContent("Updated content");

        assertThat(note.getLastModified()).isAfter(firstModified);
    }

    @Test
    @DisplayName("Should properly manage tags")
    void testTagManagement() {
        note.addTag("Java");
        note.addTag("Programming");
        note.addTag("STUDY");

        assertThat(note.getTags())
                .hasSize(3)
                .containsExactlyInAnyOrder("java", "programming", "study"); // Tags stored as lowercase

        assertThat(note.hasTag("Java")).isTrue();
        assertThat(note.hasTag("java")).isTrue();
        assertThat(note.hasTag("JAVA")).isTrue();

        note.removeTag("Programming");

        assertThat(note.getTags()).hasSize(2);
        assertThat(note.hasTag("programming")).isFalse();
    }

    @Test
    @DisplayName("Should ignore null or empty tags")
    void testInvalidTags() {
        note.addTag(null);
        note.addTag("");
        note.addTag("   ");

        assertThat(note.getTags()).isEmpty();
    }

    @Test
    @DisplayName("Should set title and update lastModified")
    void testTitleSetting() {
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        note.setTitle("My Study Notes");

        assertThat(note.getTitle()).isEqualTo("My Study Notes");
        assertThat(note.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should handle category assignment")
    void testCategoryAssignment() {
        Category category = new Category("Programming", "#4CAF50");
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        note.setCategory(category);

        assertThat(note.getCategory()).isEqualTo(category);
        assertThat(note.getCategory().getName()).isEqualTo("Programming");
        assertThat(note.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should maintain dateAdded on updates")
    void testDateAddedPersistence() {
        LocalDateTime originalDateAdded = note.getDateAdded();

        // Make various changes
        note.setTitle("Test");
        note.setContent("Content");
        note.addTag("tag");

        // dateAdded should remain unchanged
        assertThat(note.getDateAdded()).isEqualTo(originalDateAdded);
    }
}
