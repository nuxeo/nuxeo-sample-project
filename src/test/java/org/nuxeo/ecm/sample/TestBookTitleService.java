/*
 * (C) Copyright 2006-2012 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 *     Georges Racinet, jcarsique
 */

package org.nuxeo.ecm.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.test.TransactionalFeature;
import org.nuxeo.project.sample.BookTitleService;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.RuntimeFeature;

import com.google.inject.Inject;

/**
 * The main purpose of this test case is to demonstrate the use of
 * {@link RuntimeFeature}.
 *
 * It just loads the book service definition and checks that the configuration
 * system works as expected. It does not test the default configuration,
 * however.
 *
 */
@RunWith(FeaturesRunner.class)
@Features({TransactionalFeature.class, RuntimeFeature.class})
@Deploy({
// deployment of the whole nuxeo-project sample bundle
        "org.nuxeo.project.sample",
        // A test contribution, i.e, used in the scope of these tests only
        "org.nuxeo.project.sample.tests:sample-booktitle-test.xml" })
public class TestBookTitleService {

    @Inject
    protected BookTitleService service;

    @Test
    public void testServiceRegistration() throws Exception {
        assertNotNull(service);
    }

    @Test
    public void testServiceContribution() throws Exception {
        assertEquals("FOOBAR Test", service.correctTitle("foobar"));
    }
}
