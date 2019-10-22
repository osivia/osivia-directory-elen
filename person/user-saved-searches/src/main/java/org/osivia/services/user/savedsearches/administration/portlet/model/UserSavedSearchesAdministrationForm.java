package org.osivia.services.user.savedsearches.administration.portlet.model;

import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * User saved searches administration form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserSavedSearchesAdministrationForm {

    /**
     * User saved searches.
     */
    private List<UserSavedSearch> savedSearches;


    /**
     * Constructor.
     */
    public UserSavedSearchesAdministrationForm() {
        super();
    }


    public List<UserSavedSearch> getSavedSearches() {
        return savedSearches;
    }

    public void setSavedSearches(List<UserSavedSearch> savedSearches) {
        this.savedSearches = savedSearches;
    }

}
