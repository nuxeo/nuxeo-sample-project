package org.nuxeo.project.sample;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookWorkflowTask implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List<String> actors;

    protected String newTitle;

    public BookWorkflowTask() {
    }

    public BookWorkflowTask(List<String> actors) {
        this.actors = actors;
    }

    public BookWorkflowTask(String actor) {
        actors = Collections.singletonList(actor);
    }

    public BookWorkflowTask(List<String> actors, String newTitle) {
        this.actors = actors;
        this.newTitle = newTitle;
    }

    public BookWorkflowTask(String actor, String newTitle) {
        this.actors = Collections.singletonList(actor);
        this.newTitle = newTitle;
    }

    public List<String> getActors() {
        if (actors == null) {
            actors = new ArrayList<String>();
        }
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public String getDirective() {
        return newTitle;
    }

    public void setDirective(String directive) {
        this.newTitle = directive;
    }

}
