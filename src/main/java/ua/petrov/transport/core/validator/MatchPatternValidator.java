package ua.petrov.transport.core.validator;


import ua.petrov.transport.core.validator.annotation.MatchPattern;
import ua.petrov.transport.core.validator.api.IValidator;

import java.lang.annotation.Annotation;

public class MatchPatternValidator implements IValidator {

	@Override
	public String validate(Annotation ann, Object obj) {
		MatchPattern annPattern = (MatchPattern) ann;
		if (!String.valueOf(obj).matches(annPattern.pattern())) {
			return annPattern.message();
		}
		return null;
	}

}
