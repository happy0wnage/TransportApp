package ua.petrov.transport.core.util;

import java.sql.Time;
import java.time.LocalTime;

/**
 * Created by Владислав on 17.01.2016.
 */
public class TimeUtil {

    public static Time getSqlTime(long time) {
        if (LocalTime.MAX.toSecondOfDay() < time) {
            long newTime = time - LocalTime.MAX.toSecondOfDay();
            return Time.valueOf(LocalTime.ofSecondOfDay(newTime));
        }
        return Time.valueOf(LocalTime.ofSecondOfDay(time));
    }

}
