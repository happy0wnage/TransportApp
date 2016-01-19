package ua.petrov.transport.core.entity.util;

/**
 * Created by Владислав on 06.12.2015.
 */
public enum Direction {

    STRAIGHT, BACK;

    public static Direction rotate(Direction direction) {
        if (direction.equals(STRAIGHT)) {
            return BACK;
        } else {
            return STRAIGHT;
        }
    }
}
