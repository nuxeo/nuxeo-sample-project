package org.nuxeo.project.sample;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.jboss.seam.annotations.remoting.WebRemote;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.project.sample.BookManagerBean.BookInfo;

/**
 * Interface for the <code>bookManager</code> Seam component.
 * <p>
 * Use of this interface would be of course necessary if the Seam component was
 * also an EJB3. In that latter case, the <code>WebRemote</code> annotation has
 * to be put on the interface method, instead of the implementation method.
 * </p>
 */
public interface BookManager {

    public void prePassivate();

    public void postActivate();

    public void destroy();

    public List<SelectItem> getAvailableKeywords() throws ClientException;

    public List<String> getKeywords();

    public void setKeywords(List<String> keywords);

    public void changeData() throws ClientException;

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

    public String validateWizard() throws ClientException;

    public void resetKeywordValues();

    public int getRating();

    public void setRating(int rating);

    public void validation(FacesContext context, UIComponent component,
            Object value);

    public DocumentModelList getSearchResults() throws Exception;

    public String getParentTitle() throws ClientException;

    public String duplicateSiblings() throws ClientException;

    public List<BookInfo> getBooksInFolder() throws ClientException;

    public BookResultsProviderFarm.KeywordCriteria getKeywordCriteria();

    public boolean hasFilter();

    public String getFilter();

    public void setFilter(String filter);

    /**
     * Method to demonstrate Seam Remoting.
     * <p>
     * The annotation has to be on the interface in EJB3 situation. Otherwise it
     * has to be on the component implementation.
     * </p>
     * 
     * @param param parameter used from the javascript code.
     * @return something that uses the parameter
     */
    @WebRemote
    public String something(String param);

}
