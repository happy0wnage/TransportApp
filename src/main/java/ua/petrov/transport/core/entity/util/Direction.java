package ua.petrov.transport.core.entity.util;

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
