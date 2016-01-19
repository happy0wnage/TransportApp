package ua.petrov.transport.simulation.controller;

import java.util.Arrays;

/**
 * Created by Владислав on 03.01.2016.
 */
public class EventFactory {

    public static long getFasterEvent(long... events) {
        return Arrays.stream(events).filter(event -> event > 0).min().getAsLong();
    }

}
