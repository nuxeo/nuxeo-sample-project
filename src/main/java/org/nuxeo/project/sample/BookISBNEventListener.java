package org.nuxeo.project.sample;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.CoreEvent;
import org.nuxeo.ecm.core.listener.AbstractEventListener;
import org.nuxeo.ecm.core.listener.AsynchronousEventListener;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.runtime.api.Framework;

public class BookISBNEventListener extends AbstractEventListener implements
        AsynchronousEventListener {

    public void notifyEvent(CoreEvent coreEvent) throws ClientException {
        DocumentModel doc = (DocumentModel) coreEvent.getSource();
        String type = doc.getType();
        if (type.equals("Book")) {
            process(doc);
        }
    }

    public void process(DocumentModel doc) throws ClientException {
        String isbn = (String) doc.getProperty("book", "isbn");
        String title = (String) doc.getProperty("dublincore", "title");
        if (isbn == null || title == null || isbn.trim().equals("")
                || title.trim().equals("")) {
            return;
        }

        DirectoryService dirService;
        try {
            // This should work but doesn't in 5.1.3
            // dirService = Framework.getService(DirectoryService.class);
            dirService = Framework.getLocalService(DirectoryService.class);
        } catch (Exception e) {
            throw new ClientException(e);
        }

        Session dir = null;
        try {
            dir = dirService.open("book_keywords");
            DocumentModel entry = dir.getEntry(isbn);
            if (entry == null) {
                // create
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", isbn);
                map.put("label", title);
                dir.createEntry(map);
            } else {
                // update
                entry.setProperty("vocabulary", "label", title);
                dir.updateEntry(entry);
            }
        } finally {
            if (dir != null) {
                dir.close();
            }
        }
    }

}
