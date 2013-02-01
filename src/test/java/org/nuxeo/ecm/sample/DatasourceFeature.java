/*
 * (C) Copyright 2013 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Julien Carsique
 *
 */

package org.nuxeo.ecm.sample;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.TransactionalFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;
import org.nuxeo.runtime.test.runner.SimpleFeature;

/**
 * @since 5.7
 */
@Features({ TransactionalFeature.class, CoreFeature.class })
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
        }
        super.stop(runner);
    }

}
