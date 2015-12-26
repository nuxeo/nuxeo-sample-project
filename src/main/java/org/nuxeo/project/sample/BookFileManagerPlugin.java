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

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.filemanager.service.extension.AbstractFileImporter;
import org.nuxeo.ecm.platform.filemanager.utils.FileManagerUtils;
import org.nuxeo.ecm.platform.types.TypeManager;
import org.nuxeo.runtime.api.Framework;

public class BookFileManagerPlugin extends AbstractFileImporter {

    private static final long serialVersionUID = 1L;

    public DocumentModel create(CoreSession documentManager, Blob content, String path, boolean overwrite,
            String filename, TypeManager typeService) throws IOException {

        String title = FileManagerUtils.fetchTitle(FileManagerUtils.fetchFileName(filename));

        BookTitleService service = Framework.getService(BookTitleService.class);

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
