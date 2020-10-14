package org.osivia.services.user.savedsearches.administration.portlet.repository;

import org.osivia.directory.v2.model.preferences.UserSavedSearch;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.util.List;
import java.util.Map;

/**
 * User saved searches administration portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface UserSavedSearchesAdministrationRepository {

    /**
     * Get user categorized saved searches.
     *
     * @param portalControllerContext portal controller context
     * @return categorized saved searches
     */
    Map<String, List<UserSavedSearch>> getUserCategorizedSavedSearches(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get user saved searches.
     *
     * @param portalControllerContext portal controller context
     * @param categoryId              category identifier
     * @return saved searches
     */
    List<UserSavedSearch> getUserSavedSearches(PortalControllerContext portalControllerContext, String categoryId) throws PortletException;


    /**
     * Save user saved searches.
     *
     * @param portalControllerContext portal controller context
     * @param categoryId              category identifier
     * @param savedSearches           saved searches
     */
    void saveUserSavedSearches(PortalControllerContext portalControllerContext, String categoryId, List<UserSavedSearch> savedSearches) throws PortletException;

}
