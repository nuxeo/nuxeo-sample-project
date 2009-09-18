package org.nuxeo.project.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.CoreEvent;
import org.nuxeo.ecm.core.listener.AbstractEventListener;
import org.nuxeo.ecm.core.listener.AsynchronousEventListener;

public class BookEventListener extends AbstractEventListener implements
        AsynchronousEventListener {

    private static final Log log = LogFactory.getLog(BookEventListener.class);

    public void notifyEvent(CoreEvent coreEvent) {
        DocumentModel doc = (DocumentModel) coreEvent.getSource();
        String type = doc.getType();
        if (type.equals("Book")) {
            try {
                process(doc);
            } catch (ClientException e) {
                log.error(e);
            }
        }
    }

    private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

    public void process(DocumentModel doc) throws ClientException {
        doc.setProperty("dublincore", "title", "Super Book");
        String date = fmt.format(new Date());
        doc.setProperty("dublincore", "description", "(Created on "+date+")");
    }

}
