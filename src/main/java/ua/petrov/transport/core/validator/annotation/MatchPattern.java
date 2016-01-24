package ua.petrov.transport.core.validator.annotation;


import ua.petrov.transport.core.validator.MatchPatternValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = MatchPatternValidator.class)
public @interface MatchPattern {
	String pattern();

	String message() default "value doesn't match rules";

}
