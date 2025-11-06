package com.documentvault.settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Setting class.
 */
class SettingTest {

    private Setting<String> stringSetting;
    private Setting<Integer> intSetting;
    private Setting<Boolean> boolSetting;

    @BeforeEach
    void setUp() {
        stringSetting = new Setting<>("test.string", "default");
        intSetting = new Setting<>("test.int", 42);
        boolSetting = new Setting<>("test.bool", true);
    }

    @Test
    void testConstructor() {
        assertEquals("test.string", stringSetting.getKey());
        assertEquals("default", stringSetting.getValue());
        assertEquals("default", stringSetting.getDefaultValue());
    }

    @Test
    void testConstructorWithNullKey() {
        assertThrows(NullPointerException.class, () -> {
            new Setting<>(null, "value");
        });
    }

    @Test
    void testConstructorWithNullDefaultValue() {
        assertThrows(NullPointerException.class, () -> {
            new Setting<>("key", null);
        });
    }

    @Test
    void testSetValue() {
        stringSetting.setValue("new value");
        assertEquals("new value", stringSetting.getValue());
    }

    @Test
    void testSetValueWithNull() {
        assertThrows(NullPointerException.class, () -> {
            stringSetting.setValue(null);
        });
    }

    @Test
    void testReset() {
        stringSetting.setValue("changed");
        assertEquals("changed", stringSetting.getValue());

        stringSetting.reset();
        assertEquals("default", stringSetting.getValue());
    }

    @Test
    void testChangeListenerNotified() {
        AtomicInteger callCount = new AtomicInteger(0);
        String[] capturedValue = new String[1];

        stringSetting.addChangeListener(value -> {
            callCount.incrementAndGet();
            capturedValue[0] = value;
        });

        stringSetting.setValue("new value");

        assertEquals(1, callCount.get());
        assertEquals("new value", capturedValue[0]);
    }

    @Test
    void testChangeListenerNotNotifiedForSameValue() {
        AtomicInteger callCount = new AtomicInteger(0);

        stringSetting.addChangeListener(value -> callCount.incrementAndGet());

        // Set same value
        stringSetting.setValue("default");

        // Listener should not be called
        assertEquals(0, callCount.get());
    }

    @Test
    void testMultipleChangeListeners() {
        AtomicInteger count1 = new AtomicInteger(0);
        AtomicInteger count2 = new AtomicInteger(0);

        stringSetting.addChangeListener(value -> count1.incrementAndGet());
        stringSetting.addChangeListener(value -> count2.incrementAndGet());

        stringSetting.setValue("new value");

        assertEquals(1, count1.get());
        assertEquals(1, count2.get());
    }

    @Test
    void testRemoveChangeListener() {
        AtomicInteger callCount = new AtomicInteger(0);
        java.util.function.Consumer<String> listener = value -> callCount.incrementAndGet();

        stringSetting.addChangeListener(listener);
        stringSetting.setValue("value1");
        assertEquals(1, callCount.get());

        stringSetting.removeChangeListener(listener);
        stringSetting.setValue("value2");
        assertEquals(1, callCount.get()); // Should not increment
    }

    @Test
    void testChangeListenerException() {
        // Add a listener that throws an exception
        stringSetting.addChangeListener(value -> {
            throw new RuntimeException("Test exception");
        });

        // Should not throw, just log the error
        assertDoesNotThrow(() -> stringSetting.setValue("new value"));
        assertEquals("new value", stringSetting.getValue());
    }

    @Test
    void testIntegerSetting() {
        assertEquals(42, intSetting.getValue());
        intSetting.setValue(100);
        assertEquals(100, intSetting.getValue());
        intSetting.reset();
        assertEquals(42, intSetting.getValue());
    }

    @Test
    void testBooleanSetting() {
        assertTrue(boolSetting.getValue());
        boolSetting.setValue(false);
        assertFalse(boolSetting.getValue());
        boolSetting.reset();
        assertTrue(boolSetting.getValue());
    }

    @Test
    void testToString() {
        String str = stringSetting.toString();
        assertTrue(str.contains("test.string"));
        assertTrue(str.contains("default"));
    }

    @Test
    void testAddNullListener() {
        // Should not throw
        assertDoesNotThrow(() -> stringSetting.addChangeListener(null));
    }
}
