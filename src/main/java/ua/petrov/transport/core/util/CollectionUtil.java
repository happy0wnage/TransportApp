package ua.petrov.transport.core.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author Vladyslav
 */
public class CollectionUtil {

    public static boolean isNotEmpty(Collection<?> collection) {
        return !collection.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !map.isEmpty();
    }

    private CollectionUtil() {}
}
