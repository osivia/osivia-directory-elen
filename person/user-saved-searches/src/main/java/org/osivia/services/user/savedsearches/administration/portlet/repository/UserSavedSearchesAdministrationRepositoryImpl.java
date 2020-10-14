package org.osivia.services.user.savedsearches.administration.portlet.repository;

import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.List;
import java.util.Map;

/**
 * User saved searches administration portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see UserSavedSearchesAdministrationRepository
 */
@Repository
public class UserSavedSearchesAdministrationRepositoryImpl implements UserSavedSearchesAdministrationRepository {

    /**
     * User preferences service.
     */
    @Autowired
    private UserPreferencesService userPreferencesService;


    /**
     * Constructor.
     */
    public UserSavedSearchesAdministrationRepositoryImpl() {
        super();
    }


    @Override
    public Map<String, List<UserSavedSearch>> getUserCategorizedSavedSearches(PortalControllerContext portalControllerContext) throws PortletException {
        // User preferences
        UserPreferences userPreferences = this.getUserPreferences(portalControllerContext);

        return userPreferences.getCategorizedSavedSearches();
    }


    @Override
    public List<UserSavedSearch> getUserSavedSearches(PortalControllerContext portalControllerContext, String categoryId) throws PortletException {
        // User preferences
        UserPreferences userPreferences = this.getUserPreferences(portalControllerContext);

        return userPreferences.getSavedSearches(categoryId);
    }


    @Override
    public void saveUserSavedSearches(PortalControllerContext portalControllerContext, String categoryId, List<UserSavedSearch> savedSearches) throws PortletException {
        // User preferences
        UserPreferences userPreferences = this.getUserPreferences(portalControllerContext);

        userPreferences.setSavedSearches(categoryId, savedSearches);
        userPreferences.setUpdated(true);
    }


    /**
     * Get user preferences.
     *
     * @param portalControllerContext portal controller context
     * @return user preferences
     */
    private UserPreferences getUserPreferences(PortalControllerContext portalControllerContext) throws PortletException {
        // User preferences
        UserPreferences userPreferences;
        try {
            userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return userPreferences;
    }

}
