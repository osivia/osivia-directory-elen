package org.osivia.directory.v2.service.preferences;

import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.IDirService;

/**
 * User preferences service interface.
 *
 * @author Cédric Krommenhoek
 * @see IDirService
 */
public interface UserPreferencesService extends IDirService {

    /**
     * Get user preferences.
     *
     * @param portalControllerContext portal controller context
     * @return user preferences
     */
    UserPreferences getUserPreferences(PortalControllerContext portalControllerContext) throws PortalException;


    /**
     * Get user preferences.
     *
     * @param portalControllerContext portal controller context
     * @param uid                     user identifier
     * @return user preferences
     */
    UserPreferences getUserPreferences(PortalControllerContext portalControllerContext, String uid) throws PortalException;


    /**
     * Create user saved search.
     *
     * @param portalControllerContext portal controller context
     * @param id                      saved search identifier
     * @return saved search
     */
    UserSavedSearch createUserSavedSearch(PortalControllerContext portalControllerContext, int id) throws PortalException;


    /**
     * Save user preferences.
     *
     * @param portalControllerContext portal controller context
     * @param userPreferences         user preferences
     */
    void saveUserPreferences(PortalControllerContext portalControllerContext, UserPreferences userPreferences) throws PortalException;

}
