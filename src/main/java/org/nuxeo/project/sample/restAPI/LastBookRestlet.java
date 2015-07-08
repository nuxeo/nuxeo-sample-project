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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMDocumentFactory;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;
import org.nuxeo.ecm.platform.ui.web.restAPI.BaseNuxeoRestlet;
import org.nuxeo.runtime.api.Framework;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;

/**
 * This Restlet get the latest created Book document and send back a custom JSon object to the client.
 *
 * @author ldoguin
 */
public class LastBookRestlet extends BaseNuxeoRestlet {

    public static final String LAST_BOOK_PROVIDER = "LAST_BOOK";

    /**
     * override the handle method to do custom json serialization.
     */
    @Override
    public void handle(Request req, Response res) {
        DOMDocumentFactory domfactory = new DOMDocumentFactory();
        DOMDocument result = (DOMDocument) domfactory.createDocument();

        try (CoreSession session = CoreInstance.openCoreSession(null, getUserPrincipal(req))) {

            PageProviderService pps = Framework.getService(PageProviderService.class);

            PageProviderDefinition ppd = pps.getPageProviderDefinition(LAST_BOOK_PROVIDER);
            HashMap<String, Serializable> props = new HashMap<String, Serializable>();
            props.put(CoreQueryDocumentPageProvider.CORE_SESSION_PROPERTY, (Serializable) session);

            PageProvider<?> provider = pps.getPageProvider(LAST_BOOK_PROVIDER, ppd, null, null, Long.valueOf(1),
                    Long.valueOf(0), props);

            // fetch result
            List<?> dms = provider.getCurrentPage();
            if (dms.size() == 0) {
                handleError(result, res, "No result available");
                return;
            }
            DocumentModel lastBookDm = (DocumentModel) dms.get(0);
            // serialize and write in response
            String json = serialize(lastBookDm);
            res.setEntity(json, MediaType.TEXT_PLAIN);
        } catch (NuxeoException e) {
            handleError(res, e);
        }
    }

    private String serialize(DocumentModel lastBookDm) throws PropertyException {
        String jSonString = "{\"book\": {" + "\"title\": \"" + lastBookDm.getPropertyValue("dc:title") + "\","
                + "\"isbn\": \"" + lastBookDm.getPropertyValue("bk:isbn") + "\"," + "\"rating\": \""
                + lastBookDm.getPropertyValue("bk:rating") + "\"," + "\"publicationDate\": \""
                + lastBookDm.getPropertyValue("bk:publicationDate") + "\"," + "\"keywords\": \""
                + lastBookDm.getPropertyValue("bk:keywords") + "\"}}";
        return jSonString;
    }

}
