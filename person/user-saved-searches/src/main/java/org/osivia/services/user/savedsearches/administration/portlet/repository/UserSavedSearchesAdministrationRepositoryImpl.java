package org.osivia.services.user.savedsearches.administration.portlet.repository;

import org.apache.commons.collections.CollectionUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.user.UserPreferences;
import org.osivia.portal.api.user.UserSavedSearch;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.ArrayList;
import java.util.List;

/**
 * User saved searches administration portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see UserSavedSearchesAdministrationRepository
 */
@Repository
public class UserSavedSearchesAdministrationRepositoryImpl implements UserSavedSearchesAdministrationRepository {

    /**
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    public UserSavedSearchesAdministrationRepositoryImpl() {
        super();
    }


    @Override
    public List<UserSavedSearch> getUserSavedSearches(PortalControllerContext portalControllerContext) throws PortletException {
        // User preferences
        UserPreferences userPreferences = this.getUserPreferences(portalControllerContext);

        return userPreferences.getSavedSearches();
    }


    @Override
    public void saveUserSavedSearches(PortalControllerContext portalControllerContext, List<UserSavedSearch> savedSearches) throws PortletException {
        // User preferences
        UserPreferences userPreferences = this.getUserPreferences(portalControllerContext);

        userPreferences.setSavedSearches(savedSearches);
        userPreferences.setUpdate(true);
    }


    /**
     * Get user preferences.
     *
     * @param portalControllerContext portal controller context
     * @return user preferences
     */
    private UserPreferences getUserPreferences(PortalControllerContext portalControllerContext) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // User preferences
        UserPreferences userPreferences;
        try {
            userPreferences = cmsService.getUserPreferences(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return userPreferences;
    }


    // FIXME
    @Override
    public void tmpAddData(PortalControllerContext portalControllerContext) {
        UserPreferences userPreferences = null;
        try {
            userPreferences = this.getUserPreferences(portalControllerContext);
        } catch (PortletException e) {
            e.printStackTrace();
        }

        List<UserSavedSearch> savedSearches = userPreferences.getSavedSearches();

        int max = 0;
        if (CollectionUtils.isEmpty(savedSearches)) {
            savedSearches = new ArrayList<>(1);
        } else {
            for (UserSavedSearch savedSearch : savedSearches) {
                max = Math.max(max, savedSearch.getId());
            }
        }


        UserSavedSearch savedSearch = new UserSavedSearch(max + 1);
        savedSearch.setDisplayName("Filtre #" + savedSearch.getId());
        savedSearch.setData("data");
        savedSearches.add(savedSearch);

        userPreferences.setSavedSearches(savedSearches);
        userPreferences.setUpdate(true);
    }
}
