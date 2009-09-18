package org.nuxeo.project.sample;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class BookIntegerValidator implements Validator {

    public void validate(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {
        Integer v = (Integer) value;
        if ((v.intValue() % 7) != 0) {
            FacesMessage message = new FacesMessage();
            message.setDetail("The value must be a multiple of 7");
            message.setSummary("Not a multiple of 7");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }

}
