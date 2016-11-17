package org.nuxeo.sample;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.io.marshallers.json.document.DocumentModelJsonWriter;
import org.nuxeo.ecm.core.io.marshallers.json.AbstractJsonWriterTest;
import org.nuxeo.ecm.core.io.marshallers.json.JsonAssert;
import org.nuxeo.ecm.core.io.registry.context.RenderingContext.CtxBuilder;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features({PlatformFeature.class})
@Deploy({"org.nuxeo.sample.nuxeo-sample-project"})
public class SampleDocumentEnricherTest extends AbstractJsonWriterTest.Local<DocumentModelJsonWriter, DocumentModel> {

    public SampleDocumentEnricherTest() {
        super(DocumentModelJsonWriter.class, DocumentModel.class);
    }

    @Inject
    private CoreSession session;

    @Test
    public void test() throws Exception {
        DocumentModel obj = session.getDocument(new PathRef("/"));
        JsonAssert json = jsonAssert(obj, CtxBuilder.enrich("document", SampleDocumentEnricher.NAME).get());
        json = json.has("contextParameters").isObject();
        json.properties(1);
        json.has(SampleDocumentEnricher.NAME).isObject();
    }
}
