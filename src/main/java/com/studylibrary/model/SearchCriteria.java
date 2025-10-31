package com.studylibrary.model;

import java.util.Set;

/**
 * Record representing search criteria for library items.
 * Java 25: Using records for value objects to reduce boilerplate and improve
 * performance.
 */
public record SearchCriteria(
        String query,
        Category category,
        LibraryItem.ItemType type,
        Set<String> tags) {

    /**
     * Compact constructor with validation.
     */
    public SearchCriteria {
        // Normalize query
        query = query != null ? query.trim().toLowerCase() : "";

        // Ensure tags is not null
        tags = tags != null ? Set.copyOf(tags) : Set.of();
    }

    /**
     * Convenience constructor for simple text search.
     */
    public SearchCriteria(String query) {
        this(query, null, null, Set.of());
    }

    /**
     * Check if this criteria matches a library item.
     * Java 25: Enhanced pattern matching with sealed classes.
     */
    public boolean matches(LibraryItem item) {
        return matchesQuery(item) &&
                matchesCategory(item) &&
                matchesType(item) &&
                matchesTags(item);
    }

    private boolean matchesQuery(LibraryItem item) {
        if (query.isEmpty())
            return true;
        return item.getSearchableText().toLowerCase().contains(query);
    }

    private boolean matchesCategory(LibraryItem item) {
        if (category == null)
            return true;
        return category.equals(item.getCategory());
    }

    private boolean matchesType(LibraryItem item) {
        if (type == null)
            return true;
        return type.equals(item.getItemType());
    }

    private boolean matchesTags(LibraryItem item) {
        if (tags.isEmpty())
            return true;
        return item.getTags().stream().anyMatch(tags::contains);
    }

    /**
     * Returns an enhanced description using instanceof and sealed classes.
     * Java 25: Optimized with sealed class exhaustiveness checking.
     */
    public String getItemTypeIcon(LibraryItem item) {
        // Java 25: Exhaustive instanceof with sealed classes
        if (item instanceof Note note) {
            return note.isMarkdown() ? "📝✨" : "📝";
        } else if (item instanceof PdfDocument pdf) {
            return pdf.getPageCount() > 100 ? "📚" : "📄";
        } else if (item instanceof MediaLink media) {
            return media.getDurationMinutes() > 60 ? "🎬" : "🎵";
        } else if (item instanceof TextSnippet) {
            return "💻";
        }
        // Java 25: Sealed classes guarantee exhaustiveness
        throw new AssertionError("Unknown LibraryItem type: " + item.getClass());
    }

    /**
     * Get enhanced item description with metadata.
     * Java 25: Type-safe pattern matching with sealed classes.
     */
    public String getEnhancedDescription(LibraryItem item) {
        if (item instanceof Note note) {
            if (note.getContent() != null && note.getContent().length() > 1000) {
                return "Long note (" + note.getContentPreview() + ")";
            }
            return "Note: " + note.getContentPreview();
        } else if (item instanceof PdfDocument pdf) {
            if (pdf.getPageCount() > 0) {
                return "PDF: " + pdf.getFileName() + " (" + pdf.getPageCount() + " pages)";
            }
            return "PDF: " + pdf.getFileName();
        } else if (item instanceof MediaLink media) {
            if (media.getDurationMinutes() > 0) {
                return "Media: " + media.getMediaType().getDisplayName() +
                        " (" + media.getDurationFormatted() + ")";
            }
            return "Media: " + media.getMediaType().getDisplayName();
        } else if (item instanceof TextSnippet snippet) {
            if (snippet.getLanguage() != null && !snippet.getLanguage().equals("text")) {
                return "Snippet (" + snippet.getLanguage() + "): " + snippet.getContentPreview();
            }
            return "Snippet: " + snippet.getContentPreview();
        }
        // Java 25: Sealed classes guarantee this is unreachable
        throw new AssertionError("Unknown LibraryItem type: " + item.getClass());
    }
}