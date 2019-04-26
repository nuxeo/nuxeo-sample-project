package org.nuxeo.sample;

import static org.nuxeo.ecm.core.io.registry.reflect.Instantiations.SINGLETON;
import static org.nuxeo.ecm.core.io.registry.reflect.Priorities.REFERENCE;

import java.io.IOException;
import java.util.Collections;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.io.marshallers.json.enrichers.AbstractJsonEnricher;
import org.nuxeo.ecm.core.io.registry.reflect.Setup;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Enrich {@link nuxeo.ecm.core.api.DocumentModel} Json.
 * <p>
 * Format is:
 *
 * <pre>
 * {@code
 * {
 *   ...
 *   "contextParameters": {
 *     "sample_document": { ... }
 *   }
 * }
 * </pre>
 * </p>
 */
@Setup(mode = SINGLETON, priority = REFERENCE)
public class SampleDocumentEnricher extends AbstractJsonEnricher<DocumentModel> {

    public static final String NAME = "sample_document";

    public SampleDocumentEnricher() {
        super(NAME);
    }

    @Override
    public void write(JsonGenerator jg, DocumentModel obj) throws IOException {
        // How to instanciate a Session if `obj` is a DocumentModel
        //try (SessionWrapper wrapper = ctx.getSession(obj)) {
        //    CoreSession session = wrapper.getSession();
        //    ...
        //}

        jg.writeFieldName(NAME);
        jg.writeObject(Collections.EMPTY_MAP);
    }
}
