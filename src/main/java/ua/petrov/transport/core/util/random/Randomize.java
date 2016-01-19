package ua.petrov.transport.core.util.random;

import java.time.LocalTime;
import java.util.Random;

/**
 * Created by Владислав on 29.11.2015.
 */
public class Randomize {

    public static long randomTime(LocalTime from, LocalTime to) {
        Random rand = new Random();
        int toSeconds = to.toSecondOfDay();
        int fromSeconds = from.toSecondOfDay();
        return rand.nextInt(toSeconds - fromSeconds) + fromSeconds;
    }

    public static long randomWaitingTime() {
        Random random = new Random();
        int maxSeconds = 2500;
        int minSeconds = 600;
        return random.nextInt(maxSeconds - minSeconds) + minSeconds;
    }
}
