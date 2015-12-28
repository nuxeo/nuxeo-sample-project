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
 *     Georges Racinet, jcarsique
 */

package org.nuxeo.ecm.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.project.sample.BookTitleService;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.RuntimeFeature;

/**
 * The main purpose of this test case is to demonstrate the use of {@link RuntimeFeature}. It just loads the book
 * service definition and checks that the configuration system works as expected. It does not test the default
 * configuration, however.
 */
@RunWith(FeaturesRunner.class)
@Features(RuntimeFeature.class)
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
