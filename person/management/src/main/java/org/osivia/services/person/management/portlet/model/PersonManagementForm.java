package org.osivia.services.person.management.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonManagementForm {

    /** Search filter. */
    private String filter;
    /** Selected user identifier. */
    private String selectedUserId;
    
    
    /**
     * Constructor.
     */
    public PersonManagementForm() {
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
     * Getter for selectedUserId.
     * 
     * @return the selectedUserId
     */
    public String getSelectedUserId() {
        return selectedUserId;
    }

    /**
     * Setter for selectedUserId.
     * 
     * @param selectedUserId the selectedUserId to set
     */
    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

}
