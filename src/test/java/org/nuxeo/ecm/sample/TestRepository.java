package org.nuxeo.ecm.sample;
import java.util.Arrays;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.repository.jcr.testing.RepositoryOSGITestCase;

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
public class TestRepository extends RepositoryOSGITestCase {

    private static final String OSGI_BUNDLE_NAME = "org.nuxeo.project.sample";

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // We have to load our core contributions before opening the
        // repository
        deployContrib(OSGI_BUNDLE_NAME, "OSGI-INF/core-types-contrib.xml");
        deployContrib(OSGI_BUNDLE_NAME, "OSGI-INF/lifecycle-contrib.xml");
        deployBundle("org.nuxeo.ecm.core.event");
        openRepository();
    }

    /**
     * Create a book at the root of repository.
     * The session is saved to ensure that it works all way long.
     * @return the DocumentRef of created book
     */
    public DocumentRef createBook() throws Exception {
        DocumentModel model = coreSession.createDocumentModel("/", "a_book",
                "Book");
        model = coreSession.createDocument(model);
        coreSession.save();
        return model.getRef();
    }

    /**
     * Test life cycle policy Book documents
     * and play a bit with life cycle.
     * @throws Exception
     */
    public void testLifeCycle() throws Exception {
        DocumentModel docModel = coreSession.getDocument(createBook());
        assertNotNull(docModel);

        assertEquals("default", docModel.getLifeCyclePolicy());
        assertEquals("project", docModel.getCurrentLifeCycleState());
        docModel.followTransition("approve");
        assertEquals("approved", docModel.getCurrentLifeCycleState());
    }

    public void testBookSchema() throws Exception {
        DocumentModel docModel = coreSession.getDocument(createBook());
        assertNotNull(docModel);

        docModel.setProperty("book", "isbn", "12-ISBN-98765");
        String[] kw = new String[] { "sample", "original", "sleep" };
        docModel.setProperty("book", "keywords", kw);
        docModel.setProperty("book", "rating", new Long(15));
        Calendar cal = Calendar.getInstance();
        cal.set(2008, 1, 9);
        docModel.setProperty("book", "publicationDate", cal);

        coreSession.saveDocument(docModel);
        coreSession.save();

        // Reload from repository
        docModel = coreSession.getDocument(docModel.getRef());
        assertEquals("12-ISBN-98765", docModel.getProperty("book", "isbn"));
        assertEquals(Arrays.asList(kw),
                Arrays.asList((String[]) docModel.getProperty("book",
                        "keywords")));
        assertEquals(new Long(15L), docModel.getProperty("book", "rating"));
        Calendar cal2 = (Calendar) docModel.getProperty("book",
                "publicationDate");
        assertEquals(cal, cal2);
    }

    // This test Requires Nuxeo Core > 1.4.0 (changeUser appears in rev 30024)
    // To use it, change the dependency of nuxeo-jcr-connector-test to
    // 1.4.1-SNAPSHOT
    //    public void testSecurity() throws Exception {
    //    DocumentRef ref = createBook();
    //    DocumentModel docModel = coreSession.getDocument(ref);
    //    ACP acp = docModel.getACP();
    //    // use of the 'local' ACL
    //    ACL acl = acp.getOrCreateACL("local");
    //    acl.add(new ACE("toto", SecurityConstants.READ, true));
    //    acp.addACL(acl);
    //    acl = new ACLImpl("specific");
    //    acl.add(new ACE("toto", SecurityConstants.WRITE, true));
    //    acp.addACL(0, acl);
    //   docModel.setACP(acp, false);
    //  coreSession.saveDocument(docModel);

    //  changeUser("toto");
    //  docModel = coreSession.getDocument(ref);
    //  assertTrue(coreSession.hasPermission(ref, SecurityConstants.WRITE));
    //}

    /** Demonstrates that the repository is wiped out between two
     *  runs. A unitary flavour for this integration test.
     *
     * @throws Exception
     */
    public void testTearDown() throws Exception {
        DocumentModelList children = coreSession.getChildren(
                coreSession.getRootDocument().getRef());
        assertEquals(0, children.size());
    }

}