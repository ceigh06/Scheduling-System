package utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateTimeBuilder {
    
    private static LocalDateTime dateTime = LocalDateTime.now();

    public static String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String timestamp = LocalDateTime.now().format(formatter);
        return timestamp;
    }

    public static String getDayName() {
        return dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}
