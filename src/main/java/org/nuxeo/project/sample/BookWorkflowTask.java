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
