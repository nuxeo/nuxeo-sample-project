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

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.jboss.seam.annotations.remoting.WebRemote;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.project.sample.BookManagerBean.BookInfo;

/**
 * Interface for the <code>bookManager</code> Seam component.
 * <p>
 * Use of this interface would be of course necessary if the Seam component was also an EJB3. In that latter case, the
 * <code>WebRemote</code> annotation has to be put on the interface method, instead of the implementation method.
 * </p>
 */
public interface BookManager {

    public List<SelectItem> getAvailableKeywords();

    public List<String> getKeywords();

    public void setKeywords(List<String> keywords);

    public void changeData();

    public String getFirstName();

    public void setFirstName(String s);

    public void randomFirstName();

    public String getLastName();

    public void setLastName(String s);

    public void randomLastName();

    public String getIsbn();

    public void setIsbn(String s);

    public String toWizardPage(String page);

    public String getWizardPage();

    public String validateWizard();

    public void resetKeywordValues();

    public int getRating();

    public void setRating(int rating);

    public void validation(FacesContext context, UIComponent component, Object value);

    public DocumentModelList getSearchResults();

    public String getParentTitle();

    public String duplicateSiblings();

    public List<BookInfo> getBooksInFolder();

    public boolean hasFilter();

    public String getFilter();

    public void setFilter(String filter);

    /**
     * Method to demonstrate Seam Remoting.
     * <p>
     * The annotation has to be on the interface in EJB3 situation. Otherwise it has to be on the component
     * implementation.
     * </p>
     *
     * @param param parameter used from the javascript code.
     * @return something that uses the parameter
     */
    @WebRemote
    public String something(String param);

}
