package com.documentvault.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Generic setting class that holds a value of type T with change notification support.
 * 
 * @param <T> the type of the setting value
 */
public class Setting<T> {
    private final String key;
    private final T defaultValue;
    private T value;
    private final List<Consumer<T>> changeListeners;

    /**
     * Creates a new setting with the specified key and default value.
     * 
     * @param key the unique key for this setting
     * @param defaultValue the default value
     */
    public Setting(String key, T defaultValue) {
        this.key = Objects.requireNonNull(key, "Key cannot be null");
        this.defaultValue = Objects.requireNonNull(defaultValue, "Default value cannot be null");
        this.value = defaultValue;
        this.changeListeners = new ArrayList<>();
    }

    /**
     * Gets the setting key.
     * 
     * @return the setting key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the current value of this setting.
     * 
     * @return the current value
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the value of this setting and notifies all change listeners.
     * 
     * @param value the new value
     */
    public void setValue(T value) {
        T oldValue = this.value;
        this.value = Objects.requireNonNull(value, "Value cannot be null");
        
        // Notify listeners if value changed
        if (!Objects.equals(oldValue, value)) {
            notifyListeners(value);
        }
    }

    /**
     * Gets the default value of this setting.
     * 
     * @return the default value
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Resets this setting to its default value.
     */
    public void reset() {
        setValue(defaultValue);
    }

    /**
     * Adds a change listener that will be notified when the setting value changes.
     * 
     * @param listener the listener to add
     */
    public void addChangeListener(Consumer<T> listener) {
        if (listener != null) {
            changeListeners.add(listener);
        }
    }

    /**
     * Removes a change listener.
     * 
     * @param listener the listener to remove
     */
    public void removeChangeListener(Consumer<T> listener) {
        changeListeners.remove(listener);
    }

    /**
     * Notifies all registered change listeners.
     * 
     * @param newValue the new value
     */
    private void notifyListeners(T newValue) {
        for (Consumer<T> listener : changeListeners) {
            try {
                listener.accept(newValue);
            } catch (Exception e) {
                System.err.println("Error notifying setting change listener: " + e.getMessage());
            }
        }
    }

    @Override
    public String toString() {
        return "Setting{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", defaultValue=" + defaultValue +
                '}';
    }
}
