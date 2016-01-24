package ua.petrov.transport.core.validator;


import org.springframework.stereotype.Component;
import ua.petrov.transport.core.validator.annotation.NotNull;
import ua.petrov.transport.core.validator.api.IBeanValidator;
import ua.petrov.transport.core.validator.factory.ValidatorFactory;
import ua.petrov.transport.exception.NoAttributeAccessException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class BeanValidator<T> implements IBeanValidator<T> {

    private ValidatorFactory factory = new ValidatorFactory();

    @Override
    public Map<String, List<String>> validateBean(T bean) {
        Map<String, List<String>> exceptions = new HashMap<>();
        try {
            for (Field field : bean.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                for (Annotation ann : field.getAnnotations()) {
                    String message = factory.getValidatorBy(ann).validate(ann, field.get(bean));
                    if (ann instanceof NotNull) {
                        if (message != null) {
                            break;
                        }
                    }
                    addMistake(exceptions, field.getName(), message);
                }
            }
        } catch (IllegalAccessException e) {
            throw new NoAttributeAccessException(
                    "Field is not accessible outside.");
        }
        return exceptions;
    }

    private void addMistake(Map<String, List<String>> exceptions, String key,
                            String message) {
        if (message == null) {
            return;
        }
        try {
            exceptions.get(key).add(message);
        } catch (NullPointerException e) {
            exceptions.put(key, new ArrayList<>(Arrays.asList(message)));
        }
    }
}
