package ua.petrov.transport.core.validator;


import ua.petrov.transport.core.validator.api.IValidator;

import java.lang.annotation.Annotation;

public class ArrayOfNumbersValidator implements IValidator {
    @Override
    public String validate(Annotation ann, Object obj) {
        String[] arr = (String[]) obj;
        for (String el : arr) {
            try {
                Integer.parseInt(el);
            } catch (NumberFormatException e){
                return "Not numeric value found.";
            }
        }
        return null;
    }
}
