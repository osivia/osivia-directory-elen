package org.osivia.services.person.management.portlet.model;

import java.util.List;

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
    
    /** Users. */
    private List<User> users;

    
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

    /**
     * Getter for users.
     * 
     * @return the users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Setter for users.
     * 
     * @param users the users to set
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

}
