/*
 * (C) Copyright 2006-2009 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 * $Id$
 */

package org.nuxeo.project.sample.restAPI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMDocumentFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PagedDocumentsProvider;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.api.repository.Repository;
import org.nuxeo.ecm.core.search.api.client.querymodel.QueryModelService;
import org.nuxeo.ecm.core.search.api.client.querymodel.descriptor.QueryModelDescriptor;
import org.nuxeo.ecm.platform.syndication.restAPI.BaseQueryModelRestlet;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;

/**
 * This Restlet get the latest created Book document and send back a custom JSon
 * object to the client.
 *
 * @author ldoguin
 */
public class LastBookRestlet extends BaseQueryModelRestlet {

    private static final Log log = LogFactory.getLog(LastBookRestlet.class);

    public static final String LAST_BOOK_QM = "LAST_BOOK";

    @Override
    protected String getQueryModelName(Request req) {
        String qmName = (String) req.getAttributes().get("QMName");
        return qmName.toUpperCase();
    }

    @Override
    protected CoreSession getCoreSession(Request req, Response res,
            String repoName) {
        repoName = req.getResourceRef().getQueryAsForm().getFirstValue(
                "repo");
        return super.getCoreSession(req, res, repoName);
    }

    /**
     * override the handle method to do custom json serialization.
     */
    @Override
    public void handle(Request req, Response res) {

        DOMDocumentFactory domfactory = new DOMDocumentFactory();
        DOMDocument result = (DOMDocument) domfactory.createDocument();

        QueryModelService qmService = getQueryModelService(result, res);
        if (qmService == null) {
            return;
        }

        String qmName = LAST_BOOK_QM;

        QueryModelDescriptor qmd = qmService.getQueryModelDescriptor(qmName);
        if (qmd == null) {
            handleError(result, res, "can not find QueryModel " + qmName);
            return;
        }

        CoreSession session = getCoreSession(req, res, null);
        try {
            PagedDocumentsProvider provider = getPageDocumentsProvider(session,
                    qmd, req);

            // get Page number
            String pageS = req.getResourceRef().getQueryAsForm().getFirstValue(
                    PAGE_PARAM);
            int page = 0;
            if (pageS != null) {
                try {
                    page = Integer.parseInt(pageS);
                } catch (NumberFormatException e) {
                    page = 0;
                }
            }
            if (page >= provider.getNumberOfPages()) {
                handleError(result, res, "No Page " + page + " available");
                return;
            }

            // fetch result
            DocumentModel lastBookDm = provider.getPage(page).get(0);
            // serialize and write in response
            String json = serialize(lastBookDm);
            res.setEntity(json, MediaType.TEXT_PLAIN);
        } catch (Exception e) {
            handleError(res, e);
        } finally {
            try {
                Repository.close(session);
            } catch (Exception e) {
                log.error("Repository close failed", e);
            }
        }
    }

    private String serialize(DocumentModel lastBookDm)
            throws PropertyException, ClientException {
        String jSonString = "{\"book\": {" + "\"title\": \""
                + lastBookDm.getPropertyValue("dc:title") + "\","
                + "\"isbn\": \"" + lastBookDm.getPropertyValue("bk:isbn")
                + "\"," + "\"rating\": \""
                + lastBookDm.getPropertyValue("bk:rating") + "\","
                + "\"publicationDate\": \""
                + lastBookDm.getPropertyValue("bk:publicationDate") + "\","
                + "\"keywords\": \""
                + lastBookDm.getPropertyValue("bk:keywords") + "\"}}";
        return jSonString;
    }
}
