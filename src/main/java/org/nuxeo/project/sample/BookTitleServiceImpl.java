/*
 * (C) Copyright 2006 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Florent Guillaume
 */
package org.nuxeo.project.sample;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

public class BookTitleServiceImpl extends DefaultComponent implements BookTitleService {

    private List<BookTitleDescriptor> config = new ArrayList<BookTitleDescriptor>();

    public String correctTitle(String title) {
        boolean addInitialCap = false;
        boolean removeExtension = false;
        String addComment = null;

        for (BookTitleDescriptor d : config) {
            if (d.addInitialCap) {
                addInitialCap = true;
            }
            if (d.getRemoveExtension()) {
                removeExtension = true;
            }
            if (d.getAddComment() != null) {
                addComment = d.getAddComment();
            }
        }

        if (removeExtension) {
            int dot = title.lastIndexOf('.');
            if (dot > title.length() - 5) {
                title = title.substring(0, dot);
            }
        }
        if (addInitialCap) {
            title = title.toUpperCase();
        }
        if (addComment != null) {
            title += ' ' + addComment;
        }
        return title;
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        config.add((BookTitleDescriptor) contribution);
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        config.remove(contribution);
    }

}
