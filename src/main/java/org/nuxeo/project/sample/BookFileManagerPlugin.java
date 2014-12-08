package org.nuxeo.project.sample;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.filemanager.service.extension.AbstractFileImporter;
import org.nuxeo.ecm.platform.filemanager.utils.FileManagerUtils;
import org.nuxeo.ecm.platform.types.TypeManager;
import org.nuxeo.runtime.api.Framework;

public class BookFileManagerPlugin extends AbstractFileImporter {

    private static final long serialVersionUID = 1L;

    public DocumentModel create(CoreSession documentManager, Blob content, String path, boolean overwrite,
            String filename, TypeManager typeService) throws ClientException, IOException {

        String title = FileManagerUtils.fetchTitle(FileManagerUtils.fetchFileName(filename));

        BookTitleService service;
        try {
            service = Framework.getService(BookTitleService.class);
        } catch (Exception e) {
            throw new ClientException(e);
        }

        title = service.correctTitle(title);

        Random random = new Random(new Date().getTime());
        String randomName = String.valueOf(random.nextLong());

        DocumentModel doc = documentManager.createDocumentModel(path, randomName, "Book");
        doc.setPropertyValue("dublincore:title", title);
        doc.setPropertyValue("dublincore:description", filename);
        doc.setProperty("file", "content", content);
        doc = documentManager.createDocument(doc);
        documentManager.save();

        return doc;
    }

}