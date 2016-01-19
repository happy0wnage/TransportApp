/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package ua.petrov.transport.simulation.controller;

/**
 * Created by Владислав on 13.01.2016.
 */
public enum EventEnum {

    TIME_TO_NEXT_BUS(1), TIME_TO_STATION(2), TIME_WAITING(3);

    public int id;

    private long time;

    EventEnum(int id) {
        this.id = id;
    }

    private void setTime(long time) {
        this.time = time;
    }

    private long getTime() {
        return this.time;
    }
}
