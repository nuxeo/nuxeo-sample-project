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
