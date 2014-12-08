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

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.Test;
import org.nuxeo.ecm.core.storage.sql.TXSQLRepositoryTestCase;
import org.nuxeo.runtime.datasource.DataSourceHelper;

/**
 * Test a datasource as explained in http://doc.nuxeo.com/x/5AFc
 *
 * @since 5.7
 */
public class TestDatasourceJUnit3 extends TXSQLRepositoryTestCase {

    @Override
    protected void deployRepositoryContrib() throws Exception {
        deployBundle("org.nuxeo.runtime.datasource");
        deployContrib("org.nuxeo.project.sample.tests", "OSGI-INF/datasource-contrib.xml");
        super.deployRepositoryContrib();
    }

    @Test
    public void test() throws Exception {
        DataSource ds = DataSourceHelper.getDataSource("foo");
        try (Connection conn = ds.getConnection()) {
            // ... use the connection ...
        }
    }
}
