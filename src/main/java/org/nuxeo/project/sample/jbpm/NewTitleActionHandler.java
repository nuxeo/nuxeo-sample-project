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
 *     Nicolas Ulrich
 *
 * $Id$
 */

package org.nuxeo.project.sample.jbpm;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.jbpm.AbstractJbpmHandlerHelper;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

/**
 * Action handler used to update book title
 * 
 * @author Nicolas Ulrich
 */
public class NewTitleActionHandler extends AbstractJbpmHandlerHelper {

    private static final long serialVersionUID = 1L;

    protected NuxeoPrincipal getNuxeoPrincipal(String user) throws Exception {
        UserManager userManager = Framework.getService(UserManager.class);
        if (user.startsWith(NuxeoPrincipal.PREFIX)) {
            user = user.substring(NuxeoPrincipal.PREFIX.length());
        }
        return userManager.getPrincipal(user);
    }

    @Override
    public void execute(ExecutionContext executionContext) throws Exception {
        this.executionContext = executionContext;
        if (nuxeoHasStarted()) {

            ProcessInstance processInstance = executionContext.getProcessInstance();

            if (null != processInstance) {
                String newTitle = (String) processInstance.getContextInstance().getVariable(
                        "NewTitle");

                CoreSession coreSession = getCoreSession(getNuxeoPrincipal(getInitiator()));
                DocumentModel book = coreSession.getDocument(getDocumentRef());
                book.setPropertyValue("dublincore:title", newTitle);

                coreSession.saveDocument(book);
                coreSession.save();
            }

        }
        executionContext.getToken().signal();
    }
}
