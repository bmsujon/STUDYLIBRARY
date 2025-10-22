package com.studylibrary.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for DateUtil utility class.
 */
@DisplayName("DateUtil Tests")
class DateUtilTest {

    // ========== formatDateTime Tests ==========

    @Test
    @DisplayName("Should format date time correctly")
    void testFormatDateTime() {
        LocalDateTime date = LocalDateTime.of(2025, 10, 22, 14, 30);
        String formatted = DateUtil.formatDateTime(date);

        assertThat(formatted).isEqualTo("Oct 22, 2025 14:30");
    }

    @Test
    @DisplayName("Should handle null date time in formatDateTime")
    void testFormatDateTimeNull() {
        String formatted = DateUtil.formatDateTime(null);

        assertThat(formatted).isEqualTo("N/A");
    }

    @Test
    @DisplayName("Should format midnight correctly")
    void testFormatDateTimeMidnight() {
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0);
        String formatted = DateUtil.formatDateTime(date);

        assertThat(formatted).isEqualTo("Jan 01, 2025 00:00");
    }

    @Test
    @DisplayName("Should format with single digit day")
    void testFormatDateTimeSingleDigitDay() {
        LocalDateTime date = LocalDateTime.of(2025, 3, 5, 9, 15);
        String formatted = DateUtil.formatDateTime(date);

        assertThat(formatted).isEqualTo("Mar 05, 2025 09:15");
    }

    @Test
    @DisplayName("Should format end of year correctly")
    void testFormatDateTimeEndOfYear() {
        LocalDateTime date = LocalDateTime.of(2025, 12, 31, 23, 59);
        String formatted = DateUtil.formatDateTime(date);

        assertThat(formatted).isEqualTo("Dec 31, 2025 23:59");
    }

    // ========== formatDate Tests ==========

    @Test
    @DisplayName("Should format date without time")
    void testFormatDate() {
        LocalDateTime date = LocalDateTime.of(2025, 10, 22, 14, 30);
        String formatted = DateUtil.formatDate(date);

        assertThat(formatted).isEqualTo("Oct 22, 2025");
    }

    @Test
    @DisplayName("Should handle null date in formatDate")
    void testFormatDateNull() {
        String formatted = DateUtil.formatDate(null);

        assertThat(formatted).isEqualTo("N/A");
    }

    @Test
    @DisplayName("Should format date regardless of time value")
    void testFormatDateIgnoresTime() {
        LocalDateTime date1 = LocalDateTime.of(2025, 5, 15, 0, 0);
        LocalDateTime date2 = LocalDateTime.of(2025, 5, 15, 23, 59);

        assertThat(DateUtil.formatDate(date1)).isEqualTo("May 15, 2025");
        assertThat(DateUtil.formatDate(date2)).isEqualTo("May 15, 2025");
    }

    // ========== formatTime Tests ==========

    @Test
    @DisplayName("Should format time without date")
    void testFormatTime() {
        LocalDateTime date = LocalDateTime.of(2025, 10, 22, 14, 30);
        String formatted = DateUtil.formatTime(date);

        assertThat(formatted).isEqualTo("14:30");
    }

    @Test
    @DisplayName("Should handle null time in formatTime")
    void testFormatTimeNull() {
        String formatted = DateUtil.formatTime(null);

        assertThat(formatted).isEqualTo("N/A");
    }

    @Test
    @DisplayName("Should format midnight time")
    void testFormatTimeMidnight() {
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0);
        String formatted = DateUtil.formatTime(date);

        assertThat(formatted).isEqualTo("00:00");
    }

    @Test
    @DisplayName("Should format time with single digit hour and minute")
    void testFormatTimeSingleDigits() {
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 9, 5);
        String formatted = DateUtil.formatTime(date);

        assertThat(formatted).isEqualTo("09:05");
    }

    // ========== getRelativeTime Tests ==========

    @Test
    @DisplayName("Should return 'Just now' for very recent time")
    void testRelativeTimeJustNow() {
        LocalDateTime now = LocalDateTime.now();
        String relative = DateUtil.getRelativeTime(now);

        assertThat(relative).isEqualTo("Just now");
    }

    @Test
    @DisplayName("Should return 'Just now' for time less than 1 minute ago")
    void testRelativeTimeLessThanMinute() {
        LocalDateTime time = LocalDateTime.now().minusSeconds(30);
        String relative = DateUtil.getRelativeTime(time);

        assertThat(relative).isEqualTo("Just now");
    }

    @Test
    @DisplayName("Should return singular 'minute ago' for 1 minute")
    void testRelativeTimeOneMinute() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(1);
        String relative = DateUtil.getRelativeTime(time);

        assertThat(relative).isEqualTo("1 minute ago");
    }

    @Test
    @DisplayName("Should return plural 'minutes ago'")
    void testRelativeTimeMinutes() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(30);
        String relative = DateUtil.getRelativeTime(time);

        assertThat(relative).isEqualTo("30 minutes ago");
    }

    @Test
    @DisplayName("Should return singular 'hour ago' for 1 hour")
    void testRelativeTimeOneHour() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(60);
        String relative = DateUtil.getRelativeTime(time);

        assertThat(relative).isEqualTo("1 hour ago");
    }

    @Test
    @DisplayName("Should return plural 'hours ago'")
    void testRelativeTimeHours() {
        LocalDateTime time = LocalDateTime.now().minusHours(5);
        String relative = DateUtil.getRelativeTime(time);

        assertThat(relative).isEqualTo("5 hours ago");
    }

    @Test
    @DisplayName("Should return singular 'day ago' for 1 day")
    void testRelativeTimeOneDay() {
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        String relative = DateUtil.getRelativeTime(time);

        assertThat(relative).isEqualTo("1 day ago");
    }

    @Test
    @DisplayName("Should return plural 'days ago'")
    void testRelativeTimeDays() {
        LocalDateTime time = LocalDateTime.now().minusDays(3);
        String relative = DateUtil.getRelativeTime(time);

        assertThat(relative).isEqualTo("3 days ago");
    }

    @Test
    @DisplayName("Should return formatted date for old dates (> 7 days)")
    void testRelativeTimeOldDates() {
        LocalDateTime time = LocalDateTime.now().minusDays(10);
        String relative = DateUtil.getRelativeTime(time);

        // Should return a formatted date like "Oct 12, 2025"
        assertThat(relative).matches("\\w{3} \\d{2}, \\d{4}");
    }

    @Test
    @DisplayName("Should return formatted date for very old dates")
    void testRelativeTimeVeryOld() {
        LocalDateTime time = LocalDateTime.of(2020, 1, 1, 12, 0);
        String relative = DateUtil.getRelativeTime(time);

        assertThat(relative).isEqualTo("Jan 01, 2020");
    }

    @Test
    @DisplayName("Should handle null in getRelativeTime")
    void testRelativeTimeNull() {
        String relative = DateUtil.getRelativeTime(null);

        assertThat(relative).isEqualTo("N/A");
    }

    @Test
    @DisplayName("Should return exactly 23 hours ago at boundary")
    void testRelativeTimeBoundary23Hours() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(23 * 60);
        String relative = DateUtil.getRelativeTime(time);

        assertThat(relative).isEqualTo("23 hours ago");
    }

    @Test
    @DisplayName("Should return 6 days ago at boundary")
    void testRelativeTimeBoundary6Days() {
        LocalDateTime time = LocalDateTime.now().minusDays(6);
        String relative = DateUtil.getRelativeTime(time);

        assertThat(relative).isEqualTo("6 days ago");
    }

    @Test
    @DisplayName("Should transition to formatted date at 7 days")
    void testRelativeTime7DaysBoundary() {
        LocalDateTime time = LocalDateTime.now().minusDays(7);
        String relative = DateUtil.getRelativeTime(time);

        // At 7 days, should return formatted date
        assertThat(relative).matches("\\w{3} \\d{2}, \\d{4}");
    }

    // ========== Edge Cases ==========

    @Test
    @DisplayName("Should handle leap year dates")
    void testLeapYear() {
        LocalDateTime leapDay = LocalDateTime.of(2024, 2, 29, 12, 0);
        String formatted = DateUtil.formatDate(leapDay);

        assertThat(formatted).isEqualTo("Feb 29, 2024");
    }

    @Test
    @DisplayName("Should handle year 2000")
    void testYear2000() {
        LocalDateTime y2k = LocalDateTime.of(2000, 1, 1, 0, 0);
        String formatted = DateUtil.formatDateTime(y2k);

        assertThat(formatted).isEqualTo("Jan 01, 2000 00:00");
    }

    @Test
    @DisplayName("Should format all months correctly")
    void testAllMonths() {
        for (int month = 1; month <= 12; month++) {
            LocalDateTime date = LocalDateTime.of(2025, month, 15, 12, 0);
            String formatted = DateUtil.formatDate(date);

            assertThat(formatted).matches("\\w{3} 15, 2025");
            assertThat(formatted).contains("2025");
        }
    }
}
