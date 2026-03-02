package tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private static final DateTimeFormatter DB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getCurrentDate() {
        return LocalDate.now().format(INPUT_FORMAT);
    }

    public static String toDatabaseFormat(String inputDate) {
        try {
            LocalDate date = LocalDate.parse(inputDate, INPUT_FORMAT);
            return date.format(DB_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String fromDatabaseFormat(String dbDate) {
        try {
            LocalDate date = LocalDate.parse(dbDate, DB_FORMAT);
            return date.format(INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}