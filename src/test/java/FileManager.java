import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {


    public static String extractRandomFileName(String filePath) {
        Pattern digitPattern = Pattern.compile("\\d+");
        Matcher digitMatcher = digitPattern.matcher(filePath);
        StringBuilder result = new StringBuilder();

        while (digitMatcher.find()) {
            result.append(digitMatcher.group());
        }
        return result.toString();
    }

}
