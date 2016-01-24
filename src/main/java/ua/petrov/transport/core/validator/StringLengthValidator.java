package ua.petrov.transport.core.validator;


import ua.petrov.transport.core.validator.annotation.StringLength;
import ua.petrov.transport.core.validator.api.IValidator;

import java.lang.annotation.Annotation;

public class StringLengthValidator implements IValidator {
    @Override
    public String validate(Annotation ann, Object obj) {
        StringLength stringLength = (StringLength) ann;
        int maxLength = stringLength.length();
        String value = obj.toString();
        if (value.length() > maxLength) {
            return stringLength.message();
        }
        return null;
    }
}
