package com.documentvault.model;

/**
 * Represents a Media Link item (Audio/Video) in the study library.
 * Stores URLs to online media content.
 * Java 25: Final class for sealed hierarchy.
 */
public final class MediaLink extends LibraryItem {

    private String url;
    private MediaType mediaType;
    private int durationMinutes;
    private String source; // e.g., YouTube, Coursera, etc.

    /**
     * Enum for different media types.
     */
    public enum MediaType {
        VIDEO("Video"),
        AUDIO("Audio"),
        PODCAST("Podcast"),
        LECTURE("Lecture"),
        OTHER("Other");

        private final String displayName;

        MediaType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public MediaLink() {
        super(ItemType.MEDIA_LINK);
        this.mediaType = MediaType.VIDEO;
        this.durationMinutes = 0;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        touch();
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        touch();
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
        touch();
    }

    /**
     * Returns a formatted duration string.
     */
    public String getDurationFormatted() {
        if (durationMinutes == 0) {
            return "Unknown";
        } else if (durationMinutes < 60) {
            return durationMinutes + " min";
        } else {
            int hours = durationMinutes / 60;
            int minutes = durationMinutes % 60;
            return String.format("%dh %dm", hours, minutes);
        }
    }

    /**
     * Validates if the URL is properly formatted.
     */
    public boolean isValidUrl() {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return url.toLowerCase().startsWith("http://") ||
                url.toLowerCase().startsWith("https://");
    }

    @Override
    public String getSearchableText() {
        return super.getSearchableText() + " " +
                (url != null ? url : "") + " " +
                (source != null ? source : "") + " " +
                mediaType.getDisplayName();
    }
}
