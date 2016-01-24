package ua.petrov.transport.core.validator.factory;

import org.springframework.stereotype.Component;
import ua.petrov.transport.core.validator.annotation.Constraint;
import ua.petrov.transport.core.validator.api.IValidator;
import ua.petrov.transport.exception.ClassInitializationException;
import ua.petrov.transport.exception.NoAttributeAccessException;

import java.lang.annotation.Annotation;

@Component
public class ValidatorFactory {

	public IValidator getValidatorBy(Annotation annotation) {
		try {
			for (Annotation ann : annotation.annotationType().getAnnotations()) {
				if (ann instanceof Constraint) {
					Constraint conAnnotation = (Constraint) ann;
					return conAnnotation.validatedBy().newInstance();
				}
			}
		} catch (IllegalAccessException e) {
			throw new NoAttributeAccessException("Field is not accessible.");
		} catch (InstantiationException e) {
			throw new ClassInitializationException(
					"Class initialization failed.");
		}
		throw new IllegalArgumentException();
	}

}
