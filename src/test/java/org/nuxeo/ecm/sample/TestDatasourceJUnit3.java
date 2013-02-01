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
import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.nuxeo.ecm.core.storage.sql.SQLRepositoryTestCase;
import org.nuxeo.runtime.api.DataSourceHelper;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.jtajca.NuxeoContainer;

/**
 * Test a datasource as explained in http://doc.nuxeo.com/x/5AFc
 *
 * @since 5.7
 */
public class TestDatasourceJUnit3 extends SQLRepositoryTestCase {
    private static final String DIRECTORY = "target/test/h2";

    private static final String PROP_NAME = "ds.test.home";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Required since 5.6
        NuxeoContainer.installNaming();
        File dir = new File(DIRECTORY);
        FileUtils.deleteQuietly(dir);
        dir.mkdirs();
        Framework.getProperties().put(PROP_NAME, dir.getPath());
        deployBundle("org.nuxeo.runtime.datasource");
        deployContrib("org.nuxeo.project.sample.tests",
                "OSGI-INF/datasource-contrib.xml");
        // Required since 5.7
        fireFrameworkStarted();
    }

    @Override
    public void tearDown() throws Exception {
        if (NuxeoContainer.isInstalled()) {
            NuxeoContainer.uninstallNaming();
        }
        super.tearDown();
    }

    @Test
    public void test() throws Exception {
        DataSource ds = DataSourceHelper.getDataSource("foo");
        Connection conn = ds.getConnection();
        try {
            // ... use the connection ...
        } finally {
            conn.close();
        }
    }
}
