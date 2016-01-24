package ua.petrov.transport.core.validator.api;

import java.lang.annotation.Annotation;

public interface IValidator {
	String validate(Annotation ann, Object obj);
}

