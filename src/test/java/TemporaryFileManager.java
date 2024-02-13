import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemporaryFileManager {

    private final Path temporaryFilePath;
    private final String extractedDigitsFromFile;

    public TemporaryFileManager() {

        this.temporaryFilePath = createAndFillTempFile();
        this.extractedDigitsFromFile = extractRandomFileName();
    }

    private Path createAndFillTempFile() {
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


    private String extractRandomFileName() {
        Pattern digitPattern = Pattern.compile("\\d+");
        Matcher digitMatcher = digitPattern.matcher(temporaryFilePath.getFileName().toString());
        StringBuilder result = new StringBuilder();

        while (digitMatcher.find()) {
            result.append(digitMatcher.group());
        }
        return result.toString();
    }


    public Path getFilePath() {
        return temporaryFilePath;
    }

    public String getExtractedDigitsFromFile() {
        return extractedDigitsFromFile;
    }
}
