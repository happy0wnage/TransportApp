package ua.petrov.transport.core.validator.annotation;


import ua.petrov.transport.core.validator.StringLengthValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = StringLengthValidator.class)
public @interface StringLength {
    int length();

    String message() default "String can't be so long";
}
