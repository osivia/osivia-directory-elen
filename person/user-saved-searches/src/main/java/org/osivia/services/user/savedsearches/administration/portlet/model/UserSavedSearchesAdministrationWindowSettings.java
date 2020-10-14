package org.osivia.services.user.savedsearches.administration.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * User saved searches administration window settings.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserSavedSearchesAdministrationWindowSettings {

    /**
     * All categories indicator.
     */
    private boolean allCategories;
    /**
     * Category identifier.
     */
    private String categoryId;


    /**
     * Constructor.
     */
    public UserSavedSearchesAdministrationWindowSettings() {
        super();
    }


    public boolean isAllCategories() {
        return allCategories;
    }

    public void setAllCategories(boolean allCategories) {
        this.allCategories = allCategories;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
