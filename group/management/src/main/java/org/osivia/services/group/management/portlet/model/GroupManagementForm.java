package org.osivia.services.group.management.portlet.model;

import java.util.List;

import org.osivia.directory.v2.model.PortalGroup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Group management form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GroupManagementForm {

    /** Search filter. */
    private String filter;
    /** Selected group identifier. */
    private String selected;
    /** Displayed results indicator. */
    private boolean displayed;
    /** Groups. */
    private List<PortalGroup> groups;


    /**
     * Constructor.
     */
    public GroupManagementForm() {
        super();
    }


    /**
     * Getter for filter.
     * 
     * @return the filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Setter for filter.
     * 
     * @param filter the filter to set
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Getter for selected.
     * 
     * @return the selected
     */
    public String getSelected() {
        return selected;
    }

    /**
     * Setter for selected.
     * 
     * @param selected the selected to set
     */
    public void setSelected(String selected) {
        this.selected = selected;
    }

    /**
     * Getter for displayed.
     * 
     * @return the displayed
     */
    public boolean isDisplayed() {
        return displayed;
    }

    /**
     * Setter for displayed.
     * 
     * @param displayed the displayed to set
     */
    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    /**
     * Getter for groups.
     * 
     * @return the groups
     */
    public List<PortalGroup> getGroups() {
        return groups;
    }

    /**
     * Setter for groups.
     * 
     * @param groups the groups to set
     */
    public void setGroups(List<PortalGroup> groups) {
        this.groups = groups;
    }

}
