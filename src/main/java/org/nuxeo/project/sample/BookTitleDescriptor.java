package org.nuxeo.project.sample;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("configuration")
public class BookTitleDescriptor {

    @XNode("addInitialCapital")
    protected boolean addInitialCap;

    @XNode("addComment")
    private String addComment;

    public String getAddComment() {
        return addComment;
    }

    private boolean remove;

    @XNode("removeExtension")
    protected void setRemoveExtension(boolean remove) {
        this.remove = remove;
    }

    public boolean getRemoveExtension() {
        return remove;
    }

}
