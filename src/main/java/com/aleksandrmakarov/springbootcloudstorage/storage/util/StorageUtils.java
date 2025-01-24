package com.aleksandrmakarov.springbootcloudstorage.storage.util;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.Breadcrumb;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StorageUtils {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * Retrieves the object name without the given prefix.
     *
     * @param object The full object name.
     * @return The object name without the prefix if it starts with the prefix; otherwise, the original object name.
     */
    public static String getObjectName(String object) {

        if (!object.contains("/")) {
            return object;
        }

        String[] parts = object.split("/");

        return object.endsWith("/") ? parts[parts.length - 1] + "/" : parts[parts.length - 1];
    }

    public static String getPrefix(String object) {
        if (!object.contains("/")) {
            return "";
        }

        String[] parts = object.split("/");

        StringBuilder prefix = new StringBuilder();

        for (int i = 0; i < parts.length - 1; i++) {
            prefix.append(parts[i]).append("/");
        }


        return prefix.toString();
    }

    /**
     * Normalizes the prefix to ensure it always ends with a slash.
     * This helps prevent partial matches and maintains consistent formatting.
     *
     * @param prefix The prefix to be normalized.
     * @return A normalized prefix that ends with a slash or an empty string if the input is null or empty.
     */
    public static String normalizePrefix(final String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return "";
        }
        // Ensure the prefix ends with a slash to avoid partial matches.
        return prefix.endsWith("/") ? prefix : prefix + "/";
    }

    /**
     * Generates a list of breadcrumbs for the given prefix.
     * Each breadcrumb represents a part of the path and its corresponding hierarchy.
     *
     * @param prefix The input prefix representing a hierarchical path (e.g., "a/b/c").
     * @return An immutable list of breadcrumbs, starting with "Home" and followed by each part of the prefix.
     */
    public static List<Breadcrumb> getBreadcrumbs(final String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return Collections.emptyList();
        }

        String[] parts = prefix.split("/");

        List<Breadcrumb> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(new Breadcrumb("Home", ""));

        String currentPath = "";

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            // Build the hierarchical path for each breadcrumb.
            currentPath = currentPath.isEmpty() ? part : currentPath + "/" + part;
            breadcrumbs.add(new Breadcrumb(part, currentPath + "/"));
        }

        return Collections.unmodifiableList(breadcrumbs);
    }

    /**
     * Formats a file size in bytes into a human-readable format.
     * For example, 1024 bytes will be displayed as "1.0 KiB".
     *
     * @param bytes The file size in bytes.
     * @return A formatted string representing the size in human-readable units (e.g., "B", "KiB", "MiB").
     */
    public static String formatSize(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }


    /**
     * Formats a ZonedDateTime into a string using the specified or default format.
     *
     * @param dateTime The ZonedDateTime to format (can be null).
     * @return The formatted date-time string, or an empty string if the ZonedDateTime is null.
     */
    public static String formatZonedDateTime(ZonedDateTime dateTime) {

        if (dateTime == null) {
            return ""; // Return an empty string for null input.
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
        return dateTime.format(formatter);
    }
}

