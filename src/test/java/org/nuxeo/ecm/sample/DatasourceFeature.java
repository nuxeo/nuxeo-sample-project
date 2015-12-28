/*
 * (C) Copyright 2013-2015 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Julien Carsique
 *
 */

package org.nuxeo.ecm.sample;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;
import org.nuxeo.runtime.test.runner.SimpleFeature;

/**
 * @since 5.7
 */
@Features(CoreFeature.class)
@Deploy({ "org.nuxeo.runtime.datasource", "org.nuxeo.project.sample.tests" })
@LocalDeploy("org.nuxeo.project.sample.tests:OSGI-INF/datasource-contrib.xml")
public class DatasourceFeature extends SimpleFeature {

    private static final String DIRECTORY = "target/test/h2";

    private static final String PROP_NAME = "ds.test.home";

    private File dir;

    private String oldValue;

    @Override
    public void initialize(FeaturesRunner runner) throws Exception {
        dir = new File(DIRECTORY);
        FileUtils.deleteQuietly(dir);
        dir.mkdirs();
        oldValue = System.setProperty(PROP_NAME, dir.getPath());
        super.initialize(runner);
    }

    @Override
    public void stop(FeaturesRunner runner) throws Exception {
        FileUtils.deleteQuietly(dir);
        if (oldValue != null) {
            System.setProperty(PROP_NAME, oldValue);
        } else {
            System.clearProperty(PROP_NAME);
        }
        super.stop(runner);
    }

}
