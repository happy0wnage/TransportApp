package ua.petrov.transport.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Vladyslav
 */
public class DateUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-mm-dd");

    public static boolean isCorrectDate(String date) {
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static Date parseDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private DateUtil() {}
}
