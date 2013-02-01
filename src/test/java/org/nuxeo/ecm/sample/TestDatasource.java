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
import org.junit.runner.RunWith;
import org.nuxeo.runtime.api.DataSourceHelper;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

/**
 * Test a datasource as explained in http://doc.nuxeo.com/x/5AFc
 *
 * @since 5.7
 */
@RunWith(FeaturesRunner.class)
@Features(DatasourceFeature.class)
public class TestDatasource {

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
