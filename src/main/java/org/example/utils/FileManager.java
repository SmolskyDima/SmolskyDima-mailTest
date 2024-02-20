package org.example.utils;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {


    public static String extractFileName(Path filePath) {

        String filePathToAbsolutPath = filePath.toAbsolutePath().toString();
        Pattern digitPattern = Pattern.compile("\\d+");
        Matcher digitMatcher = digitPattern.matcher(filePathToAbsolutPath);
        StringBuilder result = new StringBuilder();

        while (digitMatcher.find()) {
            result.append(digitMatcher.group());
        }
        return result.toString();
    }

}
