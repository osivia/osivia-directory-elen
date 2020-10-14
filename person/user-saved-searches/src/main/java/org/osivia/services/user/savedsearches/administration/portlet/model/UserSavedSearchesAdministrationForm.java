package org.osivia.services.user.savedsearches.administration.portlet.model;

import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * User saved searches administration form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserSavedSearchesAdministrationForm {

    /**
     * User categorized saved searches.
     */
    private Map<String, List<UserSavedSearch>> categorizedSavedSearches;


    /**
     * Constructor.
     */
    public UserSavedSearchesAdministrationForm() {
        super();
    }


    public Map<String, List<UserSavedSearch>> getCategorizedSavedSearches() {
        return categorizedSavedSearches;
    }

    public void setCategorizedSavedSearches(Map<String, List<UserSavedSearch>> categorizedSavedSearches) {
        this.categorizedSavedSearches = categorizedSavedSearches;
    }
}
