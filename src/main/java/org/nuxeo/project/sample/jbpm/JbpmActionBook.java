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

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.jbpm.web.JbpmActions;

/**
 * Used to manage a process
 * 
 * @author Nicolas Ulrich
 */
public interface JbpmActionBook extends JbpmActions {

    String getNewTitle() throws ClientException;

    void setNewTitle(String newTitle) throws ClientException;

    void toggleShowSetTitle(ActionEvent event) throws ClientException;

    public boolean getShowSetTitleForm() throws ClientException;
    
    public boolean getCanSetTitle() throws ClientException;
    
}
