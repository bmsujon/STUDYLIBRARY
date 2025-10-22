package com.studylibrary.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for the LibraryItem abstract base class.
 * Tests common functionality inherited by all library item types.
 * Uses Note as a concrete implementation for testing.
 */
@DisplayName("LibraryItem Tests")
class LibraryItemTest {

    private LibraryItem item;

    @BeforeEach
    void setUp() {
        // Use Note as concrete implementation to test LibraryItem
        item = new Note();
    }

    @Test
    @DisplayName("Should generate valid UUID on creation")
    void testIdGeneration() {
        assertThat(item.getId())
                .isNotNull()
                .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    }

    @Test
    @DisplayName("Should initialize with current timestamps")
    void testInitialTimestamps() {
        LocalDateTime now = LocalDateTime.now();

        assertThat(item.getDateAdded()).isNotNull();
        assertThat(item.getLastModified()).isNotNull();
        assertThat(item.getDateAdded()).isBeforeOrEqualTo(now);
        assertThat(item.getLastModified()).isBeforeOrEqualTo(now);
    }

    @Test
    @DisplayName("Should initialize with empty tag set")
    void testInitialTagSet() {
        assertThat(item.getTags()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should set and get ID")
    void testIdManagement() {
        String customId = "custom-id-12345";
        item.setId(customId);

        assertThat(item.getId()).isEqualTo(customId);
    }

    @Test
    @DisplayName("Should set and get title")
    void testTitleManagement() {
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        item.setTitle("Test Title");

        assertThat(item.getTitle()).isEqualTo("Test Title");
        assertThat(item.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should set and get description")
    void testDescriptionManagement() {
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        item.setDescription("Test Description");

        assertThat(item.getDescription()).isEqualTo("Test Description");
        assertThat(item.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should set and get category")
    void testCategoryManagement() {
        Category category = new Category("Programming", "#4CAF50");
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        item.setCategory(category);

        assertThat(item.getCategory()).isEqualTo(category);
        assertThat(item.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should set and get dateAdded")
    void testDateAddedManagement() {
        LocalDateTime customDate = LocalDateTime.of(2025, 1, 1, 12, 0);

        item.setDateAdded(customDate);

        assertThat(item.getDateAdded()).isEqualTo(customDate);
    }

    @Test
    @DisplayName("Should set and get lastModified")
    void testLastModifiedManagement() {
        LocalDateTime customDate = LocalDateTime.of(2025, 1, 1, 12, 0);

        item.setLastModified(customDate);

        assertThat(item.getLastModified()).isEqualTo(customDate);
    }

    @Test
    @DisplayName("Should update lastModified when calling touch()")
    void testTouchMethod() {
        LocalDateTime before = item.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        item.touch();

        assertThat(item.getLastModified()).isAfter(before);
    }

    @Test
    @DisplayName("Should add tag in lowercase")
    void testAddTag() {
        LocalDateTime beforeAdd = LocalDateTime.now().minusSeconds(1);

        item.addTag("Java");

        assertThat(item.getTags()).containsExactly("java");
        assertThat(item.getLastModified()).isAfter(beforeAdd);
    }

    @Test
    @DisplayName("Should add multiple tags")
    void testAddMultipleTags() {
        item.addTag("Java");
        item.addTag("Programming");
        item.addTag("Tutorial");

        assertThat(item.getTags())
                .hasSize(3)
                .containsExactlyInAnyOrder("java", "programming", "tutorial");
    }

    @Test
    @DisplayName("Should trim whitespace when adding tags")
    void testAddTagWithWhitespace() {
        item.addTag("  Java  ");

        assertThat(item.getTags()).containsExactly("java");
    }

    @Test
    @DisplayName("Should ignore null tag")
    void testAddNullTag() {
        item.addTag(null);

        assertThat(item.getTags()).isEmpty();
    }

    @Test
    @DisplayName("Should ignore empty tag")
    void testAddEmptyTag() {
        item.addTag("");
        item.addTag("   ");

        assertThat(item.getTags()).isEmpty();
    }

    @Test
    @DisplayName("Should not add duplicate tags")
    void testAddDuplicateTags() {
        item.addTag("Java");
        item.addTag("JAVA");
        item.addTag("java");

        assertThat(item.getTags()).hasSize(1).containsExactly("java");
    }

    @Test
    @DisplayName("Should remove tag case-insensitively")
    void testRemoveTag() {
        item.addTag("Java");
        item.addTag("Python");
        LocalDateTime beforeRemove = LocalDateTime.now().minusSeconds(1);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        item.removeTag("JAVA");

        assertThat(item.getTags()).containsExactly("python");
        assertThat(item.getLastModified()).isAfter(beforeRemove);
    }

    @Test
    @DisplayName("Should handle removing non-existent tag")
    void testRemoveNonExistentTag() {
        item.addTag("Java");

        item.removeTag("Python");

        assertThat(item.getTags()).containsExactly("java");
    }

    @Test
    @DisplayName("Should handle removing null tag")
    void testRemoveNullTag() {
        item.addTag("Java");

        item.removeTag(null);

        assertThat(item.getTags()).containsExactly("java");
    }

    @Test
    @DisplayName("Should check if tag exists case-insensitively")
    void testHasTag() {
        item.addTag("Java");

        assertThat(item.hasTag("Java")).isTrue();
        assertThat(item.hasTag("JAVA")).isTrue();
        assertThat(item.hasTag("java")).isTrue();
        assertThat(item.hasTag("  Java  ")).isTrue();
    }

    @Test
    @DisplayName("Should return false for non-existent tag")
    void testHasTagNonExistent() {
        item.addTag("Java");

        assertThat(item.hasTag("Python")).isFalse();
    }

    @Test
    @DisplayName("Should return false for null tag in hasTag")
    void testHasTagNull() {
        item.addTag("Java");

        assertThat(item.hasTag(null)).isFalse();
    }

    @Test
    @DisplayName("Should set multiple tags at once")
    void testSetTags() {
        Set<String> tags = new HashSet<>();
        tags.add("java");
        tags.add("programming");
        tags.add("tutorial");

        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);
        item.setTags(tags);

        assertThat(item.getTags()).hasSize(3).containsExactlyInAnyOrder("java", "programming", "tutorial");
        assertThat(item.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should return defensive copy of tags")
    void testGetTagsDefensiveCopy() {
        item.addTag("Java");

        Set<String> tags = item.getTags();
        tags.add("Python"); // Modify the returned set

        // Original should be unchanged
        assertThat(item.getTags()).containsExactly("java");
    }

    @Test
    @DisplayName("Should create defensive copy when setting tags")
    void testSetTagsDefensiveCopy() {
        Set<String> tags = new HashSet<>();
        tags.add("java");

        item.setTags(tags);
        tags.add("python"); // Modify original set

        // Item's tags should be unchanged
        assertThat(item.getTags()).containsExactly("java");
    }

    @Test
    @DisplayName("Should generate searchable text with all fields")
    void testSearchableText() {
        item.setTitle("Java Tutorial");
        item.setDescription("Learn Java programming");
        item.addTag("programming");
        item.addTag("java");
        Category category = new Category("Programming");
        item.setCategory(category);

        String searchableText = item.getSearchableText();

        assertThat(searchableText).containsIgnoringCase("java tutorial");
        assertThat(searchableText).containsIgnoringCase("learn java programming");
        assertThat(searchableText).containsIgnoringCase("programming");
        assertThat(searchableText).containsIgnoringCase("java");
    }

    @Test
    @DisplayName("Should handle null fields in searchable text")
    void testSearchableTextWithNullFields() {
        item.setTitle(null);
        item.setDescription(null);
        item.setCategory(null);

        String searchableText = item.getSearchableText();

        assertThat(searchableText).isNotNull();
    }

    @Test
    @DisplayName("Should return searchable text in lowercase")
    void testSearchableTextLowercase() {
        item.setTitle("UPPERCASE TITLE");
        item.setDescription("MixedCase Description");

        String searchableText = item.getSearchableText();

        assertThat(searchableText).isEqualTo(searchableText.toLowerCase());
    }

    @Test
    @DisplayName("Should return title in toString when title is set")
    void testToStringWithTitle() {
        item.setTitle("My Note");

        assertThat(item.toString()).isEqualTo("My Note");
    }

    @Test
    @DisplayName("Should return default string in toString when title is null")
    void testToStringWithoutTitle() {
        item.setTitle(null);

        assertThat(item.toString()).startsWith("Untitled");
        assertThat(item.toString()).contains(item.getItemType().getDisplayName());
    }

    @Test
    @DisplayName("Should have correct ItemType display names")
    void testItemTypeDisplayNames() {
        assertThat(LibraryItem.ItemType.NOTE.getDisplayName()).isEqualTo("Note");
        assertThat(LibraryItem.ItemType.PDF.getDisplayName()).isEqualTo("PDF Document");
        assertThat(LibraryItem.ItemType.MEDIA_LINK.getDisplayName()).isEqualTo("Media Link");
        assertThat(LibraryItem.ItemType.TEXT_SNIPPET.getDisplayName()).isEqualTo("Text Snippet");
    }

    @Test
    @DisplayName("Should maintain separate tag sets for different instances")
    void testTagIndependence() {
        LibraryItem item1 = new Note();
        LibraryItem item2 = new Note();

        item1.addTag("java");
        item2.addTag("python");

        assertThat(item1.getTags()).containsExactly("java");
        assertThat(item2.getTags()).containsExactly("python");
    }

    @Test
    @DisplayName("Should generate unique IDs for different instances")
    void testUniqueIdGeneration() {
        LibraryItem item1 = new Note();
        LibraryItem item2 = new Note();
        LibraryItem item3 = new Note();

        assertThat(item1.getId())
                .isNotEqualTo(item2.getId())
                .isNotEqualTo(item3.getId());
        assertThat(item2.getId()).isNotEqualTo(item3.getId());
    }

    @Test
    @DisplayName("Should not update dateAdded when modifying item")
    void testDateAddedImmutable() {
        LocalDateTime originalDateAdded = item.getDateAdded();

        item.setTitle("Test");
        item.setDescription("Test");
        item.addTag("test");
        item.setCategory(new Category("Test"));

        assertThat(item.getDateAdded()).isEqualTo(originalDateAdded);
    }

    @Test
    @DisplayName("Should update lastModified when modifying properties")
    void testLastModifiedUpdates() {
        LocalDateTime initial = item.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        item.setTitle("Test");
        assertThat(item.getLastModified()).isAfter(initial);

        LocalDateTime afterTitle = item.getLastModified();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        item.setDescription("Test");
        assertThat(item.getLastModified()).isAfter(afterTitle);
    }

    @Test
    @DisplayName("Should handle empty title in toString")
    void testToStringEmptyTitle() {
        item.setTitle("");

        assertThat(item.toString()).isEmpty();
    }

    @Test
    @DisplayName("Should handle tags with special characters")
    void testTagsWithSpecialCharacters() {
        item.addTag("C++");
        item.addTag("C#");
        item.addTag("Node.js");

        assertThat(item.getTags()).containsExactlyInAnyOrder("c++", "c#", "node.js");
    }

    @Test
    @DisplayName("Should handle very long tag")
    void testVeryLongTag() {
        String longTag = "verylongtag".repeat(50);
        item.addTag(longTag);

        assertThat(item.getTags()).hasSize(1);
        assertThat(item.getTags().iterator().next()).hasSize(longTag.length());
    }

    @Test
    @DisplayName("Should handle many tags")
    void testManyTags() {
        for (int i = 0; i < 100; i++) {
            item.addTag("tag" + i);
        }

        assertThat(item.getTags()).hasSize(100);
    }

    @Test
    @DisplayName("Should handle null title in searchable text")
    void testSearchableTextNullTitle() {
        item.setTitle(null);
        item.setDescription("Description");

        String searchableText = item.getSearchableText();

        assertThat(searchableText).contains("description");
    }

    @Test
    @DisplayName("Should handle null description in searchable text")
    void testSearchableTextNullDescription() {
        item.setTitle("Title");
        item.setDescription(null);

        String searchableText = item.getSearchableText();

        assertThat(searchableText).contains("title");
    }

    @Test
    @DisplayName("Should handle setting null category")
    void testSetNullCategory() {
        item.setCategory(new Category("Test"));
        assertThat(item.getCategory()).isNotNull();

        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);
        item.setCategory(null);

        assertThat(item.getCategory()).isNull();
        assertThat(item.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should clear tags when setting empty set")
    void testClearTags() {
        item.addTag("Java");
        item.addTag("Python");

        item.setTags(new HashSet<>());

        assertThat(item.getTags()).isEmpty();
    }

    @Test
    @DisplayName("Should preserve ItemType throughout lifecycle")
    void testItemTypePreservation() {
        LibraryItem.ItemType originalType = item.getItemType();

        item.setTitle("Test");
        item.setDescription("Test");
        item.addTag("test");

        assertThat(item.getItemType()).isEqualTo(originalType);
    }

    @Test
    @DisplayName("Should allow setting ItemType")
    void testSetItemType() {
        item.setItemType(LibraryItem.ItemType.PDF);

        assertThat(item.getItemType()).isEqualTo(LibraryItem.ItemType.PDF);
    }
}
