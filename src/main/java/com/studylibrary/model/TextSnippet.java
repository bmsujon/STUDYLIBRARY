package com.studylibrary.model;

/**
 * Represents a Text Snippet item in the study library.
 * Quick text snippets with copy-to-clipboard functionality.
 */
public class TextSnippet extends LibraryItem {

    private String content;
    private String language; // Programming language or format (e.g., "java", "sql", "json")
    private String sourceUrl; // Optional source reference

    public TextSnippet() {
        super(ItemType.TEXT_SNIPPET);
        this.content = "";
        this.language = "text";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        touch();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        touch();
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        touch();
    }

    /**
     * Returns the content preview (first 100 characters).
     */
    public String getContentPreview() {
        if (content == null || content.isEmpty()) {
            return "Empty snippet";
        }
        String stripped = content.replaceAll("\\s+", " ").trim();
        return stripped.length() > 100 ? stripped.substring(0, 97) + "..." : stripped;
    }

    /**
     * Returns the line count of the content.
     */
    public int getLineCount() {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        return content.split("\n").length;
    }

    @Override
    public String getSearchableText() {
        return super.getSearchableText() + " " +
                (content != null ? content : "") + " " +
                (language != null ? language : "");
    }
}
