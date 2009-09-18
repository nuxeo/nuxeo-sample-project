package org.nuxeo.ecm.sample;
import org.nuxeo.project.sample.BookTitleService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.NXRuntimeTestCase;

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
 * The main purpose of this test case is to demonstrate the use of
 * {@link NXRuntimeTestCase}.
 *
 * It just loads the book service definition and checks that
 * the configuration system works as expected.
 * It does not test the default configuration, however.
 *
 * @author gracinet
 *
 */
public class TestBookTitleService extends NXRuntimeTestCase {

    private BookTitleService service;

    private static final String OSGI_BUNDLE_NAME = "org.nuxeo.project.sample";

    private static final String OSGI_TEST_BUNDLE = "org.nuxeo.project.sample.tests";

    public void setUp() throws Exception {
        super.setUp();
        // deployment of the whole nuxeo-project sample bundle
        deployBundle(OSGI_BUNDLE_NAME);

        service = Framework.getService(BookTitleService.class);
    }

    public void testServiceRegistration() throws Exception {
        assertNotNull(service);
    }

    public void testServiceContribution() throws Exception {
        // A test contribution, i.e, used in the scope of these tests only
        // Lookup is ensured simply by making the 'test' sub-hierarchy a
        // bundle of its own, with a MANIFEST file
        deployContrib(OSGI_TEST_BUNDLE, "sample-booktitle-test.xml");
        assertEquals("FOOBAR Test", service.correctTitle("foobar"));
    }
}
