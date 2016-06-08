package org.osivia.services.directory.workspace.portlet.model;

import java.util.List;

/**
 * Form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
public class AddForm {

    /** Member names. */
    private List<String> names;
    /** Role. */
    private Role role;


    /**
     * Constructor.
     */
    public AddForm() {
        super();
        this.role = Role.DEFAULT;
    }


    /**
     * Getter for names.
     * 
     * @return the names
     */
    public List<String> getNames() {
        return names;
    }

    /**
     * Setter for names.
     * 
     * @param names the names to set
     */
    public void setNames(List<String> names) {
        this.names = names;
    }

    /**
     * Getter for role.
     * 
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Setter for role.
     * 
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

}
