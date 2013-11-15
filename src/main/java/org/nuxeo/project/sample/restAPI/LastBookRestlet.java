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
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMDocumentFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.api.repository.Repository;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
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
 * This Restlet get the latest created Book document and send back a custom JSon
 * object to the client.
 * 
 * @author ldoguin
 */
public class LastBookRestlet extends BaseNuxeoRestlet {

	private static final Log log = LogFactory.getLog(LastBookRestlet.class);

	public static final String LAST_BOOK_PROVIDER = "LAST_BOOK";

	/**
	 * override the handle method to do custom json serialization.
	 */
	@Override
	public void handle(Request req, Response res) {

		DOMDocumentFactory domfactory = new DOMDocumentFactory();
		DOMDocument result = (DOMDocument) domfactory.createDocument();

		CoreSession session = getCoreSession(req, res);
		try {

			PageProviderService pps = Framework
					.getService(PageProviderService.class);


			PageProviderDefinition ppd = pps
					.getPageProviderDefinition(LAST_BOOK_PROVIDER);
			HashMap<String, Serializable> props = new HashMap<String, Serializable>();
			props.put(CoreQueryDocumentPageProvider.CORE_SESSION_PROPERTY,
					(Serializable) session);

			PageProvider<?> provider = pps.getPageProvider(LAST_BOOK_PROVIDER, ppd,
					null, null, Long.valueOf(1), Long.valueOf(0), props);

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

	private CoreSession getCoreSession(Request req, Response res) {
		CoreSession session;
		try {
			Repository repository = Framework.getService(
					RepositoryManager.class).getDefaultRepository();
			if (repository == null) {
				throw new ClientException("Cannot get default repository");
			}
			Map<String, Serializable> context = new HashMap<String, Serializable>();
			context.put("principal", getSerializablePrincipal(req));
			session = repository.open(context);
		} catch (Exception e) {
			handleError(res, e);
			return null;
		}
		return session;
	}
}
