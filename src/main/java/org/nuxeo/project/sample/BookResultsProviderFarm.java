package org.nuxeo.project.sample;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.PagedDocumentsProvider;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.core.api.impl.EmptyResultsProvider;
import org.nuxeo.ecm.core.search.api.client.querymodel.QueryModel;
import org.nuxeo.ecm.platform.ui.web.api.ResultsProviderFarm;
import org.nuxeo.ecm.platform.ui.web.pagination.ResultsProviderFarmUserException;
import org.nuxeo.ecm.webapp.querymodel.QueryModelActions;

@Name("bookResultsProviderFarm")
@Scope(ScopeType.SESSION)
public class BookResultsProviderFarm implements ResultsProviderFarm,
        Serializable {

    private static final long serialVersionUID = 1L;

    public static final String KEYWORD_KEY = "BOOK";

    @In(create = true)
    protected transient CoreSession documentManager;

    @In(create = true)
    protected transient QueryModelActions queryModelActions;

    @In(required = true)
    protected transient BookManager bookManager;

    public PagedDocumentsProvider getResultsProvider(String name)
            throws ClientException, ResultsProviderFarmUserException {
        return getResultsProvider(name, null);
    }

    public static final class KeywordCriteria {
        public final String keyword;

        public final Path path;

        public KeywordCriteria(Path path, String keyword) {
            this.keyword = keyword;
            this.path = path;
        }
    }

    public PagedDocumentsProvider getResultsProvider(String name,
            SortInfo sortInfo) throws ClientException,
            ResultsProviderFarmUserException {

        assert name.equals("BOOK") == true;

        if (bookManager.hasFilter() == false)
            return new EmptyResultsProvider();

        KeywordCriteria criteria = bookManager.getKeywordCriteria();

        Object[] params = new Object[] { criteria.path.toString(),
                criteria.keyword };

        QueryModel queryModel = queryModelActions.get(name);

        assert queryModel != null;

        PagedDocumentsProvider provider = queryModel.getResultsProvider(
                documentManager, params, sortInfo);

        assert provider != null;

        provider.setName(name);
        return provider;
    }

}
