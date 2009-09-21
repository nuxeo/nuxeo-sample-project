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

import javax.faces.event.ActionEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.jbpm.web.JbpmActionsBean;
import org.nuxeo.ecm.platform.ui.web.invalidations.AutomaticDocumentBasedInvalidation;

/**
 * Used to manage a process
 * 
 * @author Nicolas Ulrich
 */

// Used to override Nuxeo's 'jbpmAction'
@Install(precedence = Install.DEPLOYMENT, value = true)
@Name("jbpmActions")
@Scope(ScopeType.CONVERSATION)
@AutomaticDocumentBasedInvalidation
public class JbpmActionBookBean extends JbpmActionsBean implements
        JbpmActionBook {

    private static final long serialVersionUID = 1L;

    protected Boolean showSetTitleForm;

    public String getNewTitle() throws ClientException {
        return (String) getCurrentProcess().getContextInstance().getVariable(
                "NewTitle");
    }

    public void setNewTitle(String newTitle) throws ClientException {
        getCurrentProcess().getContextInstance().createVariable("NewTitle",
                newTitle);
    }

    @Override
    public String createProcessInstance(NuxeoPrincipal principal, String pd,
            DocumentModel dm, String endLifeCycle) throws ClientException {
        return super.createProcessInstance(principal, pd, dm, endLifeCycle);
    }

    public void toggleShowSetTitle(ActionEvent event) throws ClientException {
        showSetTitleForm = !getShowSetTitleForm();
    }

    public boolean getShowSetTitleForm() throws ClientException {
        if (showSetTitleForm == null) {
            showSetTitleForm = false;
            if (null == getNewTitle() || getNewTitle().equals("")) {
                showSetTitleForm = true;
            }
        }
        return showSetTitleForm;
    }

    public boolean getCanSetTitle() throws ClientException {
        return getCanManageProcess();
    }

}