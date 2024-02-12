import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemporaryFileCreator {

    private final Path temporaryFilePath ;
    private final String extractedDigitsFromFile;

    public TemporaryFileCreator() {

        this.temporaryFilePath  = createTempFile();
        this.extractedDigitsFromFile = extractRandomFileName(temporaryFilePath .getFileName().toString());
    }
    private Path createTempFile() {
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


    private String extractRandomFileName(String fileName) {
        Pattern digitPattern = Pattern.compile("\\d+");
        Matcher digitMatcher = digitPattern.matcher(fileName);
        StringBuilder result = new StringBuilder();

        while (digitMatcher.find()) {
            result.append(digitMatcher.group());
        }
        return result.toString();
    }


    public Path getFilePath() {
        return temporaryFilePath ;
    }

    public String getExtractedDigitsFromFile() {
        return extractedDigitsFromFile;
    }
}
