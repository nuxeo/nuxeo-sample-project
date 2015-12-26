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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

public class BookEventListener implements EventListener {

    @Override
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
        doc.setPropertyValue("dublincore:title", "Sample Book");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        doc.setPropertyValue("dublincore:description", "(Created on " + date + ")");
    }

}
