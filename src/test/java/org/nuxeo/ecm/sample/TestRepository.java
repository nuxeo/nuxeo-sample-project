/*
 * (C) Copyright 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Georges Racinet
 */

package org.nuxeo.ecm.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Calendar;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

/**
 * Example of a test case that sets up a repository, and in particular checks that our project core contributions are
 * correctly taken into account.
 */
@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@RepositoryConfig(cleanup = Granularity.METHOD)
@Deploy({
        // We could deploy all the contributions from the bundle (but this would
        // be less
        // of a "unit" test)
        "org.nuxeo.project.sample:OSGI-INF/core-types-contrib.xml",
        "org.nuxeo.project.sample:OSGI-INF/lifecycle-contrib.xml",
        // A test contribution, i.e, used in the scope of these tests only
        "org.nuxeo.project.sample.tests:sample-booktitle-test.xml" })
public class TestRepository {

    @Inject
    CoreSession session;

    @Before
    public void setUp() throws Exception {
        // ...
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
     * Tests the life cycle policy of Book documents and plays a bit with the life cycle.
     */
    @Test
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
    @Test
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
        assertEquals(Arrays.asList(kw), Arrays.asList((String[]) doc.getProperty("book", "keywords")));
        assertEquals(new Long(15L), doc.getProperty("book", "rating"));
        Calendar cal2 = (Calendar) doc.getProperty("book", "publicationDate");
        assertEquals(cal, cal2);
    }

    /**
     * Demonstrates that the repository is wiped out between two tests. (Granularity.METHOD)
     */
    @Test
    public void testTearDown() throws Exception {
        DocumentModelList children = session.getChildren(session.getRootDocument().getRef());
        assertEquals(0, children.size());
    }

}
