package org.nuxeo.project.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

public class BookEventListener implements EventListener {

    @Override
    public void handleEvent(Event event) throws ClientException {

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

    public void process(DocumentModel doc) throws ClientException {
        doc.setPropertyValue("dublincore:title", "Sample Book");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        doc.setPropertyValue("dublincore:description", "(Created on " + date
                + ")");
    }

}