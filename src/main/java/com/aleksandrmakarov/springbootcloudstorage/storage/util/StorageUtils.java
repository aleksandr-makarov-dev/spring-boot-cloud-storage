package com.aleksandrmakarov.springbootcloudstorage.storage.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class StorageUtils {

    public static String getObjectName(String prefix, String objectName) {

        if (objectName.startsWith(prefix)) {
            return objectName.substring(prefix.length());
        }

        return objectName;
    }

    public static String normalizePrefix(final String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return "";
        }

        // Ensure the prefix ends with a slash to avoid partial matches.
        return prefix.endsWith("/") ? prefix : prefix + "/";
    }

    public static String encode(final String input) {
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }

    public static String decode(final String input) {
        return URLDecoder.decode(input, StandardCharsets.UTF_8);
    }

    // Stolen from here https://stackoverflow.com/questions/3758606/how-can-i-convert-byte-size-into-a-human-readable-format-in-java/3758880#3758880
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
}
