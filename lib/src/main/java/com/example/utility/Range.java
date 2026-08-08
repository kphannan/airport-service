package com.example.utility;

import org.jspecify.annotations.NonNull;

/**
 * A generic utility class to define a numeric or comparable range and validate
 * if a given value falls within that range (inclusive).
 *
 * @param <T> The type of values to be stored in the range, which must implement {@link Comparable}.
 */
public final class Range<T extends Comparable<? super T>> {
    @NonNull
    private final T min;
    @NonNull
    private final T max;

    public Range(@NonNull final T min, @NonNull final T max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value.");
        }
        this.min = min;
        this.max = max;
    }

    public boolean isInRange(@NonNull final T value) {
        return isInRange(value, min, max);
    }

    /**
     * Static utility method to check if a value falls within the specified min and max inclusively.
     *
     * @param <T>    The comparable type of the values.
     * @param value  The value to check.
     * @param min    The lower bound (inclusive).
     * @param max    The upper bound (inclusive).
     * @return true if the value is within [min, max]; false otherwise.
     * @throws IllegalArgumentException if any argument is null or if min > max.
     */
    public static <T extends Comparable<? super T>> boolean isInRange(@NonNull final T value, @NonNull final T min, @NonNull final T max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value.");
        }
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    @NonNull
    public T getMin() {
        return min;
    }

    @NonNull
    public T getMax() {
        return max;
    }
}
