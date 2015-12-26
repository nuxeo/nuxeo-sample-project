/*
 * (C) Copyright 2006 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Florent Guillaume
 */
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
