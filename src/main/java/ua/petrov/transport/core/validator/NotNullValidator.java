package ua.petrov.transport.core.validator;


import ua.petrov.transport.core.validator.api.IValidator;

import java.lang.annotation.Annotation;

public class NotNullValidator implements IValidator {
    @Override
    public String validate(Annotation ann, Object obj) {
        if (obj == null){
            return "Object is null";
        }
        return null;
    }
}
