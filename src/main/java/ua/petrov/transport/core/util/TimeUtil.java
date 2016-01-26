package ua.petrov.transport.core.util;

import java.sql.Time;
import java.time.LocalTime;

public class TimeUtil {

    public static Time getSqlTime(long time) {
        if (LocalTime.MAX.toSecondOfDay() < time) {
            long newTime = time - LocalTime.MAX.toSecondOfDay();
            return Time.valueOf(LocalTime.ofSecondOfDay(newTime));
        }
        return Time.valueOf(LocalTime.ofSecondOfDay(time));
    }

    public static boolean isCorrectTime(String time) {
        if (time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            return true;
        }
        return false;
    }
}
