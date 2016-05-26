package org.osivia.services.directory.workspace.model;

import java.util.List;

/**
 * Local groups java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class LocalGroups {

    /** Local groups. */
    private List<LocalGroup> groups;


    /**
     * Constructor.
     */
    public LocalGroups() {
        super();
    }


    /**
     * Getter for groups.
     * 
     * @return the groups
     */
    public List<LocalGroup> getGroups() {
        return this.groups;
    }

    /**
     * Setter for groups.
     * 
     * @param groups the groups to set
     */
    public void setGroups(List<LocalGroup> groups) {
        this.groups = groups;
    }

}
