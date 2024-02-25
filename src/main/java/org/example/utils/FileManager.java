package org.example.utils;

import java.nio.file.Path;

public class FileManager {

    public static String extractFileName(Path filePath) {
        return filePath.getFileName().toString();
    }
}
