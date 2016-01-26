package ua.petrov.transport.core.validator;


import ua.petrov.transport.core.validator.annotation.NotNull;
import ua.petrov.transport.core.validator.api.IValidator;

import java.lang.annotation.Annotation;

public class NotNullValidator implements IValidator {
    @Override
    public String validate(Annotation ann, Object obj) {
        NotNull annNotNull = (NotNull) ann;
        if (obj == null){
            return annNotNull.message();
        }
        return null;
    }
}
