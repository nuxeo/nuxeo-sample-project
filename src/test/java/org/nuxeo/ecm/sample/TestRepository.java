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
 *     Georges Racinet
 */

package org.nuxeo.ecm.sample;

import java.util.Arrays;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.api.security.impl.ACLImpl;
import org.nuxeo.ecm.core.storage.sql.SQLRepositoryTestCase;

/**
 * Example of a test case that sets up a repository, and in particular checks
 * that our project core contributions are correctly taken into account.
 */
public class TestRepository extends SQLRepositoryTestCase {

    private static final String OSGI_BUNDLE_NAME = "org.nuxeo.project.sample";

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // We have to load our core contributions before opening the
        // repository
        deployContrib(OSGI_BUNDLE_NAME, "OSGI-INF/core-types-contrib.xml");
        deployContrib(OSGI_BUNDLE_NAME, "OSGI-INF/lifecycle-contrib.xml");
        // We could deploy all the contributions from the bundle (but this would
        // be less of a "unit" test) by doing:
        // deployBundle(OSGI_BUNDLE_NAME);

        openSession();
    }

    /**
     * Creates a book at the root of repository, and save the session.
     *
     * @return the DocumentRef of the created book
     */
    public DocumentRef createBook() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "a_book", "Book");
        doc = session.createDocument(doc);
        session.save();
        return doc.getRef();
    }

    /**
     * Tests the life cycle policy of Book documents and plays a bit with the
     * life cycle.
     */
    public void testLifeCycle() throws Exception {
        DocumentModel doc = session.getDocument(createBook());
        assertNotNull(doc);

        assertEquals("default", doc.getLifeCyclePolicy());
        assertEquals("project", doc.getCurrentLifeCycleState());
        doc.followTransition("approve");
        assertEquals("approved", doc.getCurrentLifeCycleState());
    }

    /**
     * Tests properties of the "book" schema.
     */
    public void testBookSchema() throws Exception {
        DocumentModel doc = session.getDocument(createBook());
        assertNotNull(doc);

        doc.setProperty("book", "isbn", "12-ISBN-98765");
        String[] kw = new String[] { "sample", "original", "sleep" };
        doc.setProperty("book", "keywords", kw);
        doc.setProperty("book", "rating", new Long(15));
        Calendar cal = Calendar.getInstance();
        cal.set(2008, 1, 9);
        doc.setProperty("book", "publicationDate", cal);

        session.saveDocument(doc);
        session.save();

        // Reload from repository
        doc = session.getDocument(doc.getRef());
        assertEquals("12-ISBN-98765", doc.getProperty("book", "isbn"));
        assertEquals(Arrays.asList(kw),
                Arrays.asList((String[]) doc.getProperty("book", "keywords")));
        assertEquals(new Long(15L), doc.getProperty("book", "rating"));
        Calendar cal2 = (Calendar) doc.getProperty("book", "publicationDate");
        assertEquals(cal, cal2);
    }

    /**
     * Tests ACLs and security.
     */
    public void testSecurity() throws Exception {
        DocumentRef docRef = createBook();
        DocumentModel doc = session.getDocument(docRef);
        ACP acp = doc.getACP();
        // use of the 'local' ACL
        ACL acl = acp.getOrCreateACL(ACL.LOCAL_ACL);
        acl.add(new ACE("toto", SecurityConstants.READ, true));
        acp.addACL(acl);
        acl = new ACLImpl("specific");
        acl.add(new ACE("toto", SecurityConstants.WRITE, true));
        acp.addACL(0, acl);
        doc.setACP(acp, false);
        session.saveDocument(doc);
        session.save();

        // reconnect as "toto"
        closeSession();
        session = openSessionAs("toto");

        doc = session.getDocument(docRef);
        assertTrue(session.hasPermission(docRef, SecurityConstants.WRITE));
    }

    /**
     * Demonstrates that the repository is wiped out between two runs.
     */
    public void testTearDown() throws Exception {
        DocumentModelList children = session.getChildren(session.getRootDocument().getRef());
        assertEquals(0, children.size());
    }

}