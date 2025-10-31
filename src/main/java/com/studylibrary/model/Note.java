package com.studylibrary.model;

/**
 * Represents a Note item in the study library.
 * Notes support rich text or markdown content.
 * Java 25: Final class for sealed hierarchy.
 */
public final class Note extends LibraryItem {

    private String content;
    private boolean isMarkdown;

    public Note() {
        super(ItemType.NOTE);
        this.content = "";
        this.isMarkdown = false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        touch();
    }

    public boolean isMarkdown() {
        return isMarkdown;
    }

    public void setMarkdown(boolean markdown) {
        isMarkdown = markdown;
        touch();
    }

    @Override
    public String getSearchableText() {
        var baseText = super.getSearchableText();
        var noteContent = content != null ? content : "";
        // Java 25 optimization: More efficient string concatenation
        return baseText.isEmpty() ? noteContent : baseText + " " + noteContent;
    }

    /**
     * Returns the content preview (first 100 characters).
     */
    public String getContentPreview() {
        if (content == null || content.isEmpty()) {
            return "Empty note";
        }
        String stripped = content.replaceAll("\\s+", " ").trim();
        return stripped.length() > 100 ? stripped.substring(0, 97) + "..." : stripped;
    }
}
