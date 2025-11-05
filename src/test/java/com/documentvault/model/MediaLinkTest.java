package com.documentvault.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for the MediaLink class.
 * Tests media link creation, URL validation, duration formatting, and media
 * type handling.
 */
@DisplayName("MediaLink Tests")
class MediaLinkTest {

    private MediaLink mediaLink;

    @BeforeEach
    void setUp() {
        mediaLink = new MediaLink();
    }

    @Test
    @DisplayName("Should create media link with valid UUID and ItemType.MEDIA_LINK")
    void testMediaLinkCreation() {
        assertThat(mediaLink.getId())
                .isNotNull()
                .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        assertThat(mediaLink.getItemType()).isEqualTo(LibraryItem.ItemType.MEDIA_LINK);
        assertThat(mediaLink.getUrl()).isNull();
        assertThat(mediaLink.getMediaType()).isEqualTo(MediaLink.MediaType.VIDEO);
        assertThat(mediaLink.getDurationMinutes()).isZero();
        assertThat(mediaLink.getSource()).isNull();
    }

    @Test
    @DisplayName("Should set and get URL")
    void testUrlManagement() {
        String url = "https://www.youtube.com/watch?v=example";
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        mediaLink.setUrl(url);

        assertThat(mediaLink.getUrl()).isEqualTo(url);
        assertThat(mediaLink.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should set and get media type")
    void testMediaTypeManagement() {
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        mediaLink.setMediaType(MediaLink.MediaType.PODCAST);

        assertThat(mediaLink.getMediaType()).isEqualTo(MediaLink.MediaType.PODCAST);
        assertThat(mediaLink.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should set and get duration in minutes")
    void testDurationManagement() {
        mediaLink.setDurationMinutes(45);

        assertThat(mediaLink.getDurationMinutes()).isEqualTo(45);
    }

    @Test
    @DisplayName("Should set and get source")
    void testSourceManagement() {
        LocalDateTime beforeSet = LocalDateTime.now().minusSeconds(1);

        mediaLink.setSource("YouTube");

        assertThat(mediaLink.getSource()).isEqualTo("YouTube");
        assertThat(mediaLink.getLastModified()).isAfter(beforeSet);
    }

    @Test
    @DisplayName("Should validate HTTPS URL as valid")
    void testValidHttpsUrl() {
        mediaLink.setUrl("https://www.example.com/video");

        assertThat(mediaLink.isValidUrl()).isTrue();
    }

    @Test
    @DisplayName("Should validate HTTP URL as valid")
    void testValidHttpUrl() {
        mediaLink.setUrl("http://www.example.com/video");

        assertThat(mediaLink.isValidUrl()).isTrue();
    }

    @Test
    @DisplayName("Should validate mixed case HTTPS URL")
    void testValidMixedCaseUrl() {
        mediaLink.setUrl("HtTpS://www.example.com/video");

        assertThat(mediaLink.isValidUrl()).isTrue();
    }

    @Test
    @DisplayName("Should invalidate null URL")
    void testInvalidNullUrl() {
        mediaLink.setUrl(null);

        assertThat(mediaLink.isValidUrl()).isFalse();
    }

    @Test
    @DisplayName("Should invalidate empty URL")
    void testInvalidEmptyUrl() {
        mediaLink.setUrl("");

        assertThat(mediaLink.isValidUrl()).isFalse();
    }

    @Test
    @DisplayName("Should invalidate URL without protocol")
    void testInvalidUrlWithoutProtocol() {
        mediaLink.setUrl("www.example.com/video");

        assertThat(mediaLink.isValidUrl()).isFalse();
    }

    @Test
    @DisplayName("Should invalidate URL with wrong protocol")
    void testInvalidUrlWithWrongProtocol() {
        mediaLink.setUrl("ftp://www.example.com/video");

        assertThat(mediaLink.isValidUrl()).isFalse();
    }

    @Test
    @DisplayName("Should format duration under 60 minutes")
    void testDurationFormattedUnder60Minutes() {
        mediaLink.setDurationMinutes(45);

        assertThat(mediaLink.getDurationFormatted()).isEqualTo("45 min");
    }

    @Test
    @DisplayName("Should format duration exactly 60 minutes as hours")
    void testDurationFormattedExactly60Minutes() {
        mediaLink.setDurationMinutes(60);

        assertThat(mediaLink.getDurationFormatted()).isEqualTo("1h 0m");
    }

    @Test
    @DisplayName("Should format duration over 60 minutes with hours and minutes")
    void testDurationFormattedOver60Minutes() {
        mediaLink.setDurationMinutes(90);

        assertThat(mediaLink.getDurationFormatted()).isEqualTo("1h 30m");
    }

    @Test
    @DisplayName("Should format long duration with multiple hours")
    void testDurationFormattedMultipleHours() {
        mediaLink.setDurationMinutes(150);

        assertThat(mediaLink.getDurationFormatted()).isEqualTo("2h 30m");
    }

    @Test
    @DisplayName("Should format zero duration as Unknown")
    void testDurationFormattedZero() {
        mediaLink.setDurationMinutes(0);

        assertThat(mediaLink.getDurationFormatted()).isEqualTo("Unknown");
    }

    @Test
    @DisplayName("Should format 1 minute duration")
    void testDurationFormatted1Minute() {
        mediaLink.setDurationMinutes(1);

        assertThat(mediaLink.getDurationFormatted()).isEqualTo("1 min");
    }

    @Test
    @DisplayName("Should format duration with no remainder minutes")
    void testDurationFormattedNoRemainderMinutes() {
        mediaLink.setDurationMinutes(120);

        assertThat(mediaLink.getDurationFormatted()).isEqualTo("2h 0m");
    }

    @Test
    @DisplayName("Should format very long duration")
    void testDurationFormattedVeryLong() {
        mediaLink.setDurationMinutes(675); // 11h 15m

        assertThat(mediaLink.getDurationFormatted()).isEqualTo("11h 15m");
    }

    @Test
    @DisplayName("Should include URL, source, and media type in searchable text")
    void testSearchableTextIncludesAllFields() {
        mediaLink.setTitle("Java Tutorial");
        mediaLink.setUrl("https://www.youtube.com/watch?v=example");
        mediaLink.setSource("YouTube");
        mediaLink.setMediaType(MediaLink.MediaType.VIDEO);
        mediaLink.addTag("programming");

        String searchableText = mediaLink.getSearchableText();

        assertThat(searchableText).containsIgnoringCase("java tutorial");
        assertThat(searchableText).containsIgnoringCase("youtube.com");
        assertThat(searchableText).containsIgnoringCase("youtube");
        assertThat(searchableText).containsIgnoringCase("video");
        assertThat(searchableText).containsIgnoringCase("programming");
    }

    @Test
    @DisplayName("Should handle null URL in searchable text")
    void testSearchableTextWithNullUrl() {
        mediaLink.setTitle("Tutorial");
        mediaLink.setUrl(null);
        mediaLink.setSource("Coursera");

        String searchableText = mediaLink.getSearchableText();

        assertThat(searchableText).containsIgnoringCase("tutorial");
        assertThat(searchableText).containsIgnoringCase("coursera");
    }

    @Test
    @DisplayName("Should handle null source in searchable text")
    void testSearchableTextWithNullSource() {
        mediaLink.setTitle("Tutorial");
        mediaLink.setUrl("https://example.com");
        mediaLink.setSource(null);

        String searchableText = mediaLink.getSearchableText();

        assertThat(searchableText).containsIgnoringCase("tutorial");
        assertThat(searchableText).containsIgnoringCase("example.com");
    }

    @Test
    @DisplayName("Should update lastModified when setting URL")
    void testLastModifiedUpdateOnUrlChange() {
        mediaLink.setUrl("https://example.com/first");
        LocalDateTime firstModified = mediaLink.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        mediaLink.setUrl("https://example.com/second");

        assertThat(mediaLink.getLastModified()).isAfter(firstModified);
    }

    @Test
    @DisplayName("Should update lastModified when setting media type")
    void testLastModifiedUpdateOnMediaTypeChange() {
        mediaLink.setMediaType(MediaLink.MediaType.VIDEO);
        LocalDateTime firstModified = mediaLink.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        mediaLink.setMediaType(MediaLink.MediaType.PODCAST);

        assertThat(mediaLink.getLastModified()).isAfter(firstModified);
    }

    @Test
    @DisplayName("Should update lastModified when setting source")
    void testLastModifiedUpdateOnSourceChange() {
        mediaLink.setSource("YouTube");
        LocalDateTime firstModified = mediaLink.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        mediaLink.setSource("Vimeo");

        assertThat(mediaLink.getLastModified()).isAfter(firstModified);
    }

    @Test
    @DisplayName("Should NOT update lastModified when setting duration")
    void testLastModifiedNotUpdatedOnDurationChange() {
        LocalDateTime beforeSet = mediaLink.getLastModified();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        mediaLink.setDurationMinutes(45);

        // Duration is metadata that shouldn't trigger lastModified update
        assertThat(mediaLink.getLastModified()).isEqualTo(beforeSet);
    }

    @Test
    @DisplayName("Should have VIDEO as default media type")
    void testDefaultMediaType() {
        assertThat(mediaLink.getMediaType()).isEqualTo(MediaLink.MediaType.VIDEO);
    }

    @Test
    @DisplayName("Should support all media types")
    void testAllMediaTypes() {
        mediaLink.setMediaType(MediaLink.MediaType.VIDEO);
        assertThat(mediaLink.getMediaType()).isEqualTo(MediaLink.MediaType.VIDEO);

        mediaLink.setMediaType(MediaLink.MediaType.AUDIO);
        assertThat(mediaLink.getMediaType()).isEqualTo(MediaLink.MediaType.AUDIO);

        mediaLink.setMediaType(MediaLink.MediaType.PODCAST);
        assertThat(mediaLink.getMediaType()).isEqualTo(MediaLink.MediaType.PODCAST);

        mediaLink.setMediaType(MediaLink.MediaType.LECTURE);
        assertThat(mediaLink.getMediaType()).isEqualTo(MediaLink.MediaType.LECTURE);

        mediaLink.setMediaType(MediaLink.MediaType.OTHER);
        assertThat(mediaLink.getMediaType()).isEqualTo(MediaLink.MediaType.OTHER);
    }

    @Test
    @DisplayName("Should return correct display names for media types")
    void testMediaTypeDisplayNames() {
        assertThat(MediaLink.MediaType.VIDEO.getDisplayName()).isEqualTo("Video");
        assertThat(MediaLink.MediaType.AUDIO.getDisplayName()).isEqualTo("Audio");
        assertThat(MediaLink.MediaType.PODCAST.getDisplayName()).isEqualTo("Podcast");
        assertThat(MediaLink.MediaType.LECTURE.getDisplayName()).isEqualTo("Lecture");
        assertThat(MediaLink.MediaType.OTHER.getDisplayName()).isEqualTo("Other");
    }

    @Test
    @DisplayName("Should handle YouTube URL")
    void testYouTubeUrl() {
        mediaLink.setUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ");

        assertThat(mediaLink.isValidUrl()).isTrue();
    }

    @Test
    @DisplayName("Should handle Vimeo URL")
    void testVimeoUrl() {
        mediaLink.setUrl("https://vimeo.com/123456789");

        assertThat(mediaLink.isValidUrl()).isTrue();
    }

    @Test
    @DisplayName("Should handle Coursera URL")
    void testCourseraUrl() {
        mediaLink.setUrl("https://www.coursera.org/learn/course-name");

        assertThat(mediaLink.isValidUrl()).isTrue();
    }

    @Test
    @DisplayName("Should handle URL with query parameters")
    void testUrlWithQueryParameters() {
        mediaLink.setUrl("https://example.com/video?id=123&time=45");

        assertThat(mediaLink.isValidUrl()).isTrue();
    }

    @Test
    @DisplayName("Should handle URL with fragment")
    void testUrlWithFragment() {
        mediaLink.setUrl("https://example.com/video#section");

        assertThat(mediaLink.isValidUrl()).isTrue();
    }

    @Test
    @DisplayName("Should handle very long URL")
    void testVeryLongUrl() {
        String longUrl = "https://example.com/very/long/path/" + "segment/".repeat(100);
        mediaLink.setUrl(longUrl);

        assertThat(mediaLink.isValidUrl()).isTrue();
    }

    @Test
    @DisplayName("Should handle URL with special characters")
    void testUrlWithSpecialCharacters() {
        mediaLink.setUrl("https://example.com/video?title=Java%20Programming");

        assertThat(mediaLink.isValidUrl()).isTrue();
    }

    @Test
    @DisplayName("Should handle negative duration gracefully")
    void testNegativeDuration() {
        mediaLink.setDurationMinutes(-10);

        // The implementation doesn't prevent negative values
        // But getDurationFormatted should handle it reasonably
        assertThat(mediaLink.getDurationMinutes()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Should handle whitespace in URL validation")
    void testUrlWithWhitespace() {
        mediaLink.setUrl("  https://example.com  ");

        // URL with leading/trailing spaces is not trimmed by implementation
        // so it will fail validation (spaces before http)
        assertThat(mediaLink.isValidUrl()).isFalse();
    }

    @Test
    @DisplayName("Should maintain property independence")
    void testPropertyIndependence() {
        mediaLink.setUrl("https://example.com");
        mediaLink.setMediaType(MediaLink.MediaType.PODCAST);
        mediaLink.setDurationMinutes(60);
        mediaLink.setSource("Spotify");

        MediaLink anotherLink = new MediaLink();

        // Verify the other link has default/null values
        assertThat(anotherLink.getUrl()).isNull();
        assertThat(anotherLink.getMediaType()).isEqualTo(MediaLink.MediaType.VIDEO);
        assertThat(anotherLink.getDurationMinutes()).isZero();
        assertThat(anotherLink.getSource()).isNull();
    }

    @Test
    @DisplayName("Should maintain dateAdded on updates")
    void testDateAddedPersistence() {
        LocalDateTime originalDateAdded = mediaLink.getDateAdded();

        // Make various changes
        mediaLink.setUrl("https://example.com");
        mediaLink.setMediaType(MediaLink.MediaType.AUDIO);
        mediaLink.setSource("YouTube");

        // dateAdded should remain unchanged
        assertThat(mediaLink.getDateAdded()).isEqualTo(originalDateAdded);
    }
}
