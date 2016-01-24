package ua.petrov.transport.core.constants;

/**
 * @author Vladyslav
 */
public class CoreConsts {

    public static class Pattern {
        private static final String INTERNATIONAL_SYMBOL = "[А-ЯA-Zа-яa-z]";

        public static final String PASS = "^[a-zA-Z0-9\\_\\-\\.]{3,16}$";
        public static final String SEAT = "^1[0-9]$|^2[0-9]$|^3[0-5]$";
        public static final String GREATER_ZERO = "^[1-9][0-9]*$";
        public static final String PRICE = "^\\d{1,2}.\\d{1,2}$";
        public static final String STRING_VAL = "^" + INTERNATIONAL_SYMBOL + "{1,20}$";
    }
}
