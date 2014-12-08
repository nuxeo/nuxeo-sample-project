package org.nuxeo.project.sample;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class BookIntegerConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Integer result;
        if (value.trim().length() == 0) {
            result = Integer.valueOf(0);
        } else {
            try {
                result = Integer.valueOf(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                FacesMessage message = new FacesMessage();
                message.setDetail("The value must be an integer");
                message.setSummary("Not an integer");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ConverterException(message);
            }
        }
        return result;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        int v = ((Integer) value).intValue();
        if (v == 0) {
            return "";
        } else {
            return Integer.toString(v);
        }
    }

}
