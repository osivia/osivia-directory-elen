package org.osivia.services.user.savedsearches.administration.portlet.repository;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.user.UserSavedSearch;

import javax.portlet.PortletException;
import java.util.List;

/**
 * User saved searches administration portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface UserSavedSearchesAdministrationRepository {

    /**
     * Get user saved searches.
     *
     * @param portalControllerContext portal controller context
     * @return saved searches
     */
    List<UserSavedSearch> getUserSavedSearches(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save user saved searches.
     *
     * @param portalControllerContext portal controller context
     * @param savedSearches           saved searches
     */
    void saveUserSavedSearches(PortalControllerContext portalControllerContext, List<UserSavedSearch> savedSearches) throws PortletException;


    // FIXME
    void tmpAddData(PortalControllerContext portalControllerContext);

}
