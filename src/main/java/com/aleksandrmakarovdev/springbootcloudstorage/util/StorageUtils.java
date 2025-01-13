package com.aleksandrmakarovdev.springbootcloudstorage.util;

public class StorageUtils {

    public static String getObjectName(String path) {

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path.substring(path.lastIndexOf("/") + 1);
    }
}
