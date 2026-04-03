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

    public static String formatTo12Hour(int hour24, int minute) {
        hour24 = hour24 % 24;

        String meridiem = hour24 >= 12 ? "PM" : "AM";
        int hour12 = hour24 % 12;
        if (hour12 == 0)
            hour12 = 12;

        return String.format("%d:%02d %s", hour12, minute, meridiem);
    }
}
