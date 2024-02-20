package org.example.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TemporaryFileCreator {
    public Path createTempFile() {
        try {
            Path tempFilePath = Files.createTempFile(null, ".txt");
            try (FileWriter writer = new FileWriter(tempFilePath.toFile())) {
                writer.write("Hello");
            }
            return tempFilePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary file.", e);
        }
    }
}