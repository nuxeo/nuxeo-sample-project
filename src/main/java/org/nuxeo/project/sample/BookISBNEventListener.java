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

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.runtime.api.Framework;

public class BookISBNEventListener implements EventListener {

    public void handleEvent(Event event) {

        EventContext ctx = event.getContext();

        if (ctx instanceof DocumentEventContext) {

            DocumentEventContext docCtx = (DocumentEventContext) ctx;
            DocumentModel doc = docCtx.getSourceDocument();

            if (doc != null) {
                String type = doc.getType();
                if ("Book".equals(type)) {
                    process(doc);
                }
            }
        }

    }

    public void process(DocumentModel doc) {
        String isbn = (String) doc.getPropertyValue("book:isbn");
        String title = (String) doc.getPropertyValue("dublincore:title");
        if (isbn == null || title == null || isbn.trim().equals("") || title.trim().equals("")) {
            return;
        }

        DirectoryService dirService = Framework.getService(DirectoryService.class);

        try (Session dir = dirService.open("book_keywords")) {
            DocumentModel entry = dir.getEntry(isbn);

            if (entry == null) {
                // create
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", isbn);
                map.put("label", title);
                dir.createEntry(map);
            } else {
                // update
                entry.setPropertyValue("vocabulary:label", title);
                dir.updateEntry(entry);
            }
        }
    }

}
