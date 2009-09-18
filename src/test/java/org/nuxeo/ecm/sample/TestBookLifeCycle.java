package org.nuxeo.ecm.sample;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.NXCore;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.listener.CoreEventListenerService;
import org.nuxeo.ecm.core.listener.EventListener;
import org.nuxeo.ecm.core.repository.jcr.testing.RepositoryOSGITestCase;
import org.nuxeo.runtime.api.Framework;

/*
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *     gracinet
 *
 * $Id$
 */

/**
 * Example of a test case that sets a repository up, and in particular
 * checks that our project core contributions are correctly taken into
 * account.
 *
 * @author gracinet
 *
 */
public class TestBookLifeCycle extends RepositoryOSGITestCase {

	private static final String OSGI_BUNDLE_NAME = "org.nuxeo.project.sample";
	private static final Log log = LogFactory.getLog(TestBookLifeCycle.class);



    /**
     * Create a book at the root of repository.
     * The session is saved to ensure that it works all way long.
     * @return the DocumentRef of created book
     */
    public DocumentRef createBook() throws Exception {
        DocumentModel model = coreSession.createDocumentModel("/", "a_book",
                "Book");
        model = coreSession.createDocument(model);
        // save the new Book to the repository on disk
        coreSession.save();
        return model.getRef();
    }

    /**
     * Test life cycle policy Book documents
     * and play a bit with life cycle.
     * @throws Exception
     */
    public void testLifeCycle() throws Exception {
        
    	// Initialize the repository
        setUp();
        // We have to load our core contributions before opening the repository
        deployContrib(OSGI_BUNDLE_NAME, "OSGI-INF/core-types-contrib.xml");
        deployContrib(OSGI_BUNDLE_NAME, "OSGI-INF/lifecycle-contrib.xml");
        openRepository();
        
        // Get the session. This gives us access to the repository and hence Documents. 
        // The coreSession is provided for us from the superclass
        assertNotNull("Does our session exist?", coreSession);
        assertNotNull("Does our repository exist?", repository);
        
        // create a Book Document
        DocumentModel docModel = coreSession.getDocument(createBook());
        assertNotNull(docModel);

        // Test that our newly created Book is attached to the "default" life cycle policy
        assertEquals("default", docModel.getLifeCyclePolicy());
        // Test that our newly created Book starts at the first state "project"
        assertEquals("project", docModel.getCurrentLifeCycleState());
        // Change the Book's Life Cycle State
        docModel.followTransition("approve");
        // Test to see that it changed to what we changed it to !
        assertEquals("approved", docModel.getCurrentLifeCycleState());
        
        // Clean up 
        tearDown();
    }


}
