package ua.petrov.transport.core.validator;


import ua.petrov.transport.core.validator.api.IValidator;

import java.lang.annotation.Annotation;

public class StringNotEmptyValidator implements IValidator {


	@Override
	public String validate(Annotation ann, Object obj) {
		if (isEmpty(obj)) {
			return "Field is empty.";
		}
		return null;
	}

	public boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		String string = (String) obj;
		return string.isEmpty();
	}

}
